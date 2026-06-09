package com.dacarex.capital.io;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class Logger {

    private static final String RUTA_LOG = "logs/dacarex.log";
    private static final DateTimeFormatter FORMATO = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

    // Clase utilitaria, no se instancia
    private Logger() {}

    public static void info(String mensaje) {
        escribir("INFO", mensaje);
    }

    public static void error(String mensaje) {
        escribir("ERROR", mensaje);
    }

    public static void advertencia(String mensaje) {
        escribir("WARN", mensaje);
    }

    private static void escribir(String nivel, String mensaje) {
        // Crear carpeta logs si no existe
        new java.io.File("logs").mkdirs();

        String linea = new StringBuilder()
                .append("[").append(LocalDateTime.now().format(FORMATO)).append("] ")
                .append("[").append(nivel).append("] ")
                .append(mensaje)
                .toString();

        // true = modo append, no sobreescribe
        try (BufferedWriter bw = new BufferedWriter(new FileWriter(RUTA_LOG, true))) {
            bw.write(linea);
            bw.newLine();
        } catch (IOException e) {
            System.err.println("No se pudo escribir en el log: " + e.getMessage());
        }
    }

    public static void limpiarLog() {
        try (BufferedWriter bw = new BufferedWriter(new FileWriter(RUTA_LOG, false))) {
            bw.write("");
        } catch (IOException e) {
            System.err.println("No se pudo limpiar el log: " + e.getMessage());
        }
    }
}