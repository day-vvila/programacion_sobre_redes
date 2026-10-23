package main.TP7;

import java.io.*;
import java.net.Socket;
import java.nio.file.*;
import java.util.Scanner;

/**
 * CLIENTE DE NUBE
 *
 * - Pide la IP del servidor y abre un Socket TCP.
 * - Ofrece una consola interactiva: LOGIN, LIST, UPLOAD, DOWNLOAD, EXIT.
 *
 * MANEJO DE STREAMS:
 *  - DataOutputStream/DataInputStream sobre el socket: texto con writeUTF/readUTF,
 *    tamaños con writeLong y el contenido del archivo como bytes crudos.
 *  - UPLOAD: FileInputStream -> bloques de bytes -> socket.
 *  - DOWNLOAD: socket -> bloques de bytes -> FileOutputStream.
 *  - Se envía primero el tamaño para que el receptor sepa cuántos bytes leer
 *    y pueda verificar la integridad al terminar.
 *
 * MANEJO DE HILOS: cada PC cliente es un proceso independiente; el servidor
 * le asigna un hilo propio. Este cliente es secuencial (un comando a la vez).
 *
 * Ejecutar: java ClienteNube [puerto]
 */
public class ClienteNube {

    private static final int TAM_BUFFER = 4096;
    private static final String CARPETA_DESCARGAS = "descargas_cliente";

    public static void main(String[] args) {
        int puerto = (args.length > 0) ? Integer.parseInt(args[0]) : 5000;
        Scanner teclado = new Scanner(System.in);

        System.out.print("IP del servidor: ");
        String ip = teclado.nextLine().trim();

        try (Socket socket = new Socket(ip, puerto);
             DataInputStream entrada = new DataInputStream(new BufferedInputStream(socket.getInputStream()));
             DataOutputStream salida = new DataOutputStream(new BufferedOutputStream(socket.getOutputStream()))) {

            System.out.println("Conectado a " + ip + ":" + puerto);
            System.out.println("Comandos: LOGIN <usuario> <clave> | LIST | UPLOAD <archivo> | DOWNLOAD <archivo> | EXIT");

            boolean seguir = true;
            while (seguir) {
                System.out.print("nube> ");
                String linea = teclado.nextLine().trim();
                if (linea.isEmpty()) continue;

                String[] p = linea.split("\\s+", 2);
                String cmd = p[0].toUpperCase();
                String arg = (p.length > 1) ? p[1].trim() : "";

                switch (cmd) {
                    case "LOGIN":
                    case "LIST":
                        enviar(salida, linea);
                        if (cmd.equals("LIST")) recibirLista(entrada);
                        else System.out.println(entrada.readUTF());
                        break;
                    case "UPLOAD":
                        subir(arg, salida, entrada);
                        break;
                    case "DOWNLOAD":
                        descargar(arg, salida, entrada);
                        break;
                    case "EXIT":
                        enviar(salida, "EXIT");
                        System.out.println(entrada.readUTF());
                        seguir = false;
                        break;
                    default:
                        System.out.println("Comando no reconocido.");
                }
            }
            System.out.println("Sesión cerrada.");
            // try-with-resources cierra streams y socket de forma limpia
        } catch (IOException e) {
            System.out.println("Error de conexión: " + e.getMessage());
        }
    }

    private static void enviar(DataOutputStream salida, String msg) throws IOException {
        salida.writeUTF(msg);
        salida.flush(); // imprescindible: los streams están con buffer
    }

    /** LIST: "OK", n, y n pares (nombre, tamaño). */
    private static void recibirLista(DataInputStream entrada) throws IOException {
        String estado = entrada.readUTF();
        if (!estado.startsWith("OK")) {
            System.out.println(estado);
            return;
        }
        int n = entrada.readInt();
        System.out.println("--- Archivos en la nube (" + n + ") ---");
        for (int i = 0; i < n; i++) {
            String nombre = entrada.readUTF();
            long tam = entrada.readLong();
            System.out.printf("%-35s %12d bytes%n", nombre, tam);
        }
    }

    /** UPLOAD: lee el archivo local y lo transmite al servidor. */
    private static void subir(String ruta, DataOutputStream salida, DataInputStream entrada) throws IOException {
        if (ruta.isEmpty()) { System.out.println("Uso: UPLOAD <nombre_archivo>"); return; }
        Path archivo = Paths.get(ruta);
        if (!Files.isRegularFile(archivo)) {
            System.out.println("El archivo local no existe: " + ruta);
            return;
        }
        // El servidor guarda solo el nombre, sin carpetas
        String nombre = archivo.getFileName().toString();
        enviar(salida, "UPLOAD " + nombre);

        String resp = entrada.readUTF();
        if (!resp.startsWith("OK")) { System.out.println(resp); return; }

        long tamano = Files.size(archivo);
        salida.writeLong(tamano);
        long enviados = 0;
        try (InputStream in = new BufferedInputStream(Files.newInputStream(archivo))) {
            byte[] buf = new byte[TAM_BUFFER];
            int n;
            while ((n = in.read(buf)) != -1) {
                salida.write(buf, 0, n);
                enviados += n;
            }
        }
        salida.flush();
        System.out.println("Enviados " + enviados + " bytes. " + entrada.readUTF());
    }

    /** DOWNLOAD: recibe los bytes y reconstruye el archivo localmente. */
    private static void descargar(String nombre, DataOutputStream salida, DataInputStream entrada) throws IOException {
        if (nombre.isEmpty()) { System.out.println("Uso: DOWNLOAD <nombre_archivo>"); return; }
        enviar(salida, "DOWNLOAD " + nombre);

        String resp = entrada.readUTF();
        if (!resp.startsWith("OK")) { System.out.println(resp); return; }

        long tamano = entrada.readLong();
        Path carpeta = Paths.get(CARPETA_DESCARGAS);
        Files.createDirectories(carpeta);
        Path destino = carpeta.resolve(Paths.get(nombre).getFileName());

        long recibidos = 0;
        try (OutputStream out = new BufferedOutputStream(Files.newOutputStream(destino))) {
            byte[] buf = new byte[TAM_BUFFER];
            while (recibidos < tamano) {
                int aLeer = (int) Math.min(buf.length, tamano - recibidos);
                int n = entrada.read(buf, 0, aLeer);
                if (n == -1) throw new EOFException("Conexión cortada durante la descarga");
                out.write(buf, 0, n);
                recibidos += n;
            }
        }
        System.out.println("Descargado en " + destino.toAbsolutePath() + " (" + recibidos + " bytes)");
    }
}
