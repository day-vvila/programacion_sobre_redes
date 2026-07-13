package com.examen;

/**
 * Sistema de registro de errores que escribe en el archivo crash.log.
 * <p>
 * Ningun error debe mostrarse por consola, todos deben registrarse aqui.
 * Utiliza FileWriter y PrintWriter para escribir en el archivo.
 */
public class LogManager {

    /**
     * Inicializa el sistema de log.
     * Crea o verifica que el archivo crash.log existe y esta listo para escribir.
     * Este metodo debe llamarse una unica vez al iniciar el programa.
     */
    public static void inicializar() {
        // COMPLETAR: crear el archivo crash.log si no existe
    }

    /**
     * Registra un error en crash.log.
     * Escribe la fecha/hora, el mensaje de contexto y el stack trace de la excepcion.
     *
     * @param mensaje descripcion del contexto donde ocurrio el error
     * @param e       la excepcion capturada
     */
    public static void registrarError(String mensaje, Exception e) {
        // COMPLETAR: escribir mensaje y stack trace en crash.log
        // Usar FileWriter y PrintWriter
    }

    /**
     * Registra un mensaje informativo en crash.log.
     *
     * @param mensaje mensaje informativo a registrar
     */
    public static void registrarInfo(String mensaje) {
        // COMPLETAR: escribir mensaje informativo en crash.log
    }
}
