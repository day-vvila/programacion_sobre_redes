package main.TP7;

import java.io.*;
import java.net.Socket;
import java.nio.file.*;
import java.util.Map;
import java.util.stream.Stream;

/**
 * Atiende UNA sesión de cliente dentro de su propio hilo.
 *
 * MANEJO DE HILOS:
 *  - Cada instancia corre en un Thread distinto (creado en ServidorNube).
 *  - Las variables de sesión (autenticado, usuario, streams) son atributos de
 *    la instancia, por lo que NO se comparten entre clientes.
 *  - El único recurso compartido es la carpeta de la nube. Para que una
 *    descarga nunca lea un archivo a medio escribir, las subidas se guardan en
 *    un archivo temporal y luego se renombran de forma atómica.
 *
 * MANEJO DE STREAMS:
 *  - DataInputStream / DataOutputStream sobre el socket: permiten mezclar
 *    texto (writeUTF/readUTF), enteros y longs (tamaños) y bytes crudos.
 *  - Los archivos se mueven en bloques de bytes con InputStream/OutputStream
 *    (BufferedInputStream / BufferedOutputStream sobre FileXxxStream).
 *
 * PROTOCOLO (todas las respuestas comienzan con "OK" o "ERROR"):
 *   LOGIN u c      -> "OK ..." | "ERROR ..."
 *   LIST           -> "OK", int n, n * (UTF nombre, long tamaño)
 *   UPLOAD nombre  -> "OK" (listo), luego el cliente envía long tamaño + bytes,
 *                     y el servidor responde "OK ..." | "ERROR ..."
 *   DOWNLOAD nom   -> "OK", long tamaño, bytes   |  "ERROR ..."
 *   EXIT           -> "OK ..." y se cierra la conexión
 */
public class ManejadorCliente implements Runnable {

    private static final int TAM_BUFFER = 4096;

    private final Socket socket;
    private final Path carpeta;
    private final Map<String, String> usuarios;
    private final String ipRemota;

    private DataInputStream entrada;
    private DataOutputStream salida;

    // Estado de la sesión (propio de este hilo)
    private boolean autenticado = false;
    private String usuario = null;

    public ManejadorCliente(Socket socket, Path carpeta, Map<String, String> usuarios) {
        this.socket = socket;
        this.carpeta = carpeta;
        this.usuarios = usuarios;
        this.ipRemota = socket.getInetAddress().getHostAddress();
    }

    @Override
    public void run() {
        String hilo = Thread.currentThread().getName();
        try {
            entrada = new DataInputStream(new BufferedInputStream(socket.getInputStream()));
            salida = new DataOutputStream(new BufferedOutputStream(socket.getOutputStream()));

            boolean seguir = true;
            while (seguir) {
                String linea = entrada.readUTF(); // bloquea SOLO a este hilo
                log("Comando recibido: " + ocultarClave(linea));
                seguir = procesar(linea);
            }
        } catch (EOFException e) {
            log("El cliente cerró la conexión sin enviar EXIT.");
        } catch (IOException e) {
            log("Error de E/S: " + e.getMessage());
        } finally {
            try { socket.close(); } catch (IOException ignorado) { }
            System.out.println("[" + hilo + "] Sesión finalizada (" + ipRemota + ")");
        }
    }

    /** Despacha el comando. Devuelve false cuando la sesión debe terminar. */
    private boolean procesar(String linea) throws IOException {
        String[] p = linea.trim().split("\\s+", 2);
        String cmd = p[0].toUpperCase();
        String arg = (p.length > 1) ? p[1].trim() : "";

        if (cmd.equals("EXIT")) {
            responder("OK Hasta luego");
            return false;
        }
        if (cmd.equals("LOGIN")) {
            login(arg);
            return true;
        }

        // Cualquier otro comando exige autenticación previa
        if (!autenticado) {
            responder("ERROR Debe autenticarse primero con LOGIN <usuario> <clave>");
            // Si era UPLOAD, el cliente no enviará datos (espera nuestro OK), no hay que vaciar nada.
            return true;
        }

        switch (cmd) {
            case "LIST":     listar(); break;
            case "UPLOAD":   subir(arg); break;
            case "DOWNLOAD": descargar(arg); break;
            default:         responder("ERROR Comando desconocido: " + cmd);
        }
        return true;
    }

    // ------------------------------------------------------------------ LOGIN
    private void login(String arg) throws IOException {
        String[] d = arg.split("\\s+");
        if (d.length != 2) {
            responder("ERROR Uso: LOGIN <usuario> <clave>");
            return;
        }
        String clave = usuarios.get(d[0]);
        if (clave != null && clave.equals(d[1])) {
            autenticado = true;
            usuario = d[0];
            log("Login correcto de '" + usuario + "'");
            responder("OK Bienvenido " + usuario);
        } else {
            log("Login FALLIDO para '" + d[0] + "'");
            responder("ERROR Usuario o clave incorrectos");
        }
    }

    // ------------------------------------------------------------------- LIST
    private void listar() throws IOException {
        // Se arma primero la lista para enviar un conteo coherente
        java.util.List<Path> archivos = new java.util.ArrayList<>();
        try (Stream<Path> s = Files.list(carpeta)) {
            s.filter(f -> Files.isRegularFile(f) && !f.getFileName().toString().endsWith(".tmp"))
             .sorted()
             .forEach(archivos::add);
        }
        responder("OK");
        salida.writeInt(archivos.size());
        for (Path f : archivos) {
            salida.writeUTF(f.getFileName().toString());
            salida.writeLong(Files.size(f));
        }
        salida.flush();
        log("LIST enviado (" + archivos.size() + " archivos)");
    }

    // ----------------------------------------------------------------- UPLOAD
    private void subir(String nombre) throws IOException {
        Path destino = resolverSeguro(nombre);
        if (destino == null) {
            responder("ERROR Nombre de archivo inválido");
            return;
        }
        responder("OK Listo para recibir"); // el cliente ahora envía tamaño + bytes

        long tamano = entrada.readLong();
        // Temporal único por hilo: evita choques si dos clientes suben el mismo nombre
        Path temporal = carpeta.resolve(nombre + "." + Thread.currentThread().getId() + ".tmp");

        long recibidos = 0;
        try (OutputStream out = new BufferedOutputStream(Files.newOutputStream(temporal))) {
            byte[] buf = new byte[TAM_BUFFER];
            while (recibidos < tamano) {
                int aLeer = (int) Math.min(buf.length, tamano - recibidos);
                int n = entrada.read(buf, 0, aLeer);
                if (n == -1) throw new EOFException("Conexión cortada durante la subida");
                out.write(buf, 0, n);
                recibidos += n;
            }
        } catch (IOException e) {
            Files.deleteIfExists(temporal); // no dejar archivos corruptos
            throw e;
        }
        // Renombrado atómico: el archivo aparece completo o no aparece
        Files.move(temporal, destino, StandardCopyOption.REPLACE_EXISTING,
                StandardCopyOption.ATOMIC_MOVE);
        log("UPLOAD '" + nombre + "' recibido (" + recibidos + " bytes)");
        responder("OK Archivo '" + nombre + "' subido (" + recibidos + " bytes)");
    }

    // --------------------------------------------------------------- DOWNLOAD
    private void descargar(String nombre) throws IOException {
        Path origen = resolverSeguro(nombre);
        if (origen == null || !Files.isRegularFile(origen)) {
            responder("ERROR El archivo no existe en la nube");
            return;
        }
        long tamano = Files.size(origen);
        responder("OK");
        salida.writeLong(tamano);

        long enviados = 0;
        try (InputStream in = new BufferedInputStream(Files.newInputStream(origen))) {
            byte[] buf = new byte[TAM_BUFFER];
            int n;
            while ((n = in.read(buf)) != -1) {
                salida.write(buf, 0, n);
                enviados += n;
            }
        }
        salida.flush();
        log("DOWNLOAD '" + nombre + "' enviado (" + enviados + " bytes)");
    }

    // -------------------------------------------------------------- utilidades
    /** Evita "path traversal": solo nombres simples dentro de la carpeta de la nube. */
    private Path resolverSeguro(String nombre) {
        if (nombre == null || nombre.isEmpty()
                || nombre.contains("/") || nombre.contains("\\") || nombre.contains("..")) {
            return null;
        }
        Path p = carpeta.resolve(nombre).normalize();
        return p.getParent().equals(carpeta) ? p : null;
    }

    private void responder(String msg) throws IOException {
        salida.writeUTF(msg);
        salida.flush();
    }

    private String ocultarClave(String linea) {
        return linea.toUpperCase().startsWith("LOGIN")
                ? linea.replaceAll("(?i)^(LOGIN\\s+\\S+).*", "$1 ****") : linea;
    }

    private void log(String msg) {
        System.out.println("[" + Thread.currentThread().getName() + " | " + ipRemota
                + (usuario != null ? " | " + usuario : "") + "] " + msg);
    }
}
