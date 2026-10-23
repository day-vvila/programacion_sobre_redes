package main.TP7;

import java.io.IOException;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.ServerSocket;
import java.net.Socket;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Collections;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Map;

/**
 * SERVIDOR DE NUBE
 *
 * MANEJO DE HILOS:
 *  - El hilo principal (main) solo ejecuta un bucle infinito en accept().
 *  - Cada vez que llega una conexión, se crea un objeto ManejadorCliente
 *    (Runnable) y se lo ejecuta en un Thread NUEVO e independiente.
 *  - Así el hilo principal vuelve de inmediato a escuchar y ningún cliente
 *    bloquea la atención de los demás (sesiones simultáneas).
 *
 * Compilar: javac *.java
 * Ejecutar: java ServidorNube [puerto]
 */
public class ServidorNube {

    public static final int PUERTO_DEFECTO = 5000;
    public static final String CARPETA_NUBE = "nube_servidor";

    /** Credenciales válidas (solo lectura después de iniciar => seguro entre hilos). */
    private static final Map<String, String> USUARIOS;
    static {
        Map<String, String> m = new HashMap<>();
        m.put("admin", "1234");
        m.put("alumno", "abcd");
        USUARIOS = Collections.unmodifiableMap(m);
    }

    public static void main(String[] args) throws IOException {
        int puerto = (args.length > 0) ? Integer.parseInt(args[0]) : PUERTO_DEFECTO;

        // Carpeta local donde se alojan los archivos de la nube
        Path carpeta = Paths.get(CARPETA_NUBE).toAbsolutePath();
        Files.createDirectories(carpeta);

        mostrarIPsLocales();
        System.out.println("[SERVIDOR] Carpeta de nube: " + carpeta);
        System.out.println("[SERVIDOR] Escuchando en el puerto " + puerto + "...");

        int contador = 0;
        // try-with-resources: el ServerSocket se cierra solo si el programa termina
        try (ServerSocket servidor = new ServerSocket(puerto)) {
            while (true) {
                // accept() BLOQUEA solo al hilo principal hasta que llega un cliente
                Socket socket = servidor.accept();
                contador++;
                System.out.println("[SERVIDOR] Conexión #" + contador + " desde "
                        + socket.getInetAddress().getHostAddress() + ":" + socket.getPort());

                // Un hilo nuevo por cada cliente => atención concurrente
                Thread hilo = new Thread(
                        new ManejadorCliente(socket, carpeta, USUARIOS),
                        "Cliente-" + contador);
                hilo.start();
            }
        }
    }

    /** Muestra las IPs de la PC para que el cliente sepa a cuál conectarse. */
    private static void mostrarIPsLocales() {
        try {
            Enumeration<NetworkInterface> ifaces = NetworkInterface.getNetworkInterfaces();
            while (ifaces.hasMoreElements()) {
                NetworkInterface ni = ifaces.nextElement();
                if (!ni.isUp() || ni.isLoopback()) continue;
                Enumeration<InetAddress> dirs = ni.getInetAddresses();
                while (dirs.hasMoreElements()) {
                    InetAddress d = dirs.nextElement();
                    if (d instanceof java.net.Inet4Address) {
                        System.out.println("[SERVIDOR] IP disponible: " + d.getHostAddress()
                                + " (" + ni.getDisplayName() + ")");
                    }
                }
            }
        } catch (IOException e) {
            System.out.println("[SERVIDOR] No se pudieron listar las IPs: " + e.getMessage());
        }
    }
}
