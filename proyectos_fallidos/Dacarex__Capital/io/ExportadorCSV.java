package com.dacarex.capital.io;

import com.dacarex.capital.exception.PersistenciaException;
import com.dacarex.capital.model.Movimiento;

import java.io.*;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

public class ExportadorCSV {

    private static final String CABECERA = "Fecha,Tipo,Descripcion,Importe,Categoria,Notas";
    private static final String CARPETA  = "exportaciones/";

    public String exportar(List<Movimiento> movimientos) {
        // Crear carpeta si no existe
        new File(CARPETA).mkdirs();

        String nombreFichero = CARPETA + "movimientos_"
                + LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyyMMdd_HHmmss"))
                + ".csv";

        try (BufferedWriter bw = new BufferedWriter(new FileWriter(nombreFichero))) {
            bw.write(CABECERA);
            bw.newLine();

            for (Movimiento m : movimientos) {
                bw.write(m.toCsv());
                bw.newLine();
            }

            Logger.info("CSV exportado correctamente: " + nombreFichero);
            return nombreFichero;

        } catch (IOException e) {
            Logger.error("Error al exportar CSV: " + e.getMessage());
            throw new PersistenciaException("No se pudo exportar el fichero CSV.", e);
        }
    }

    public List<String> leerCSV(String rutaFichero) {
        List<String> lineas = new java.util.ArrayList<>();

        File fichero = new File(rutaFichero);
        if (!fichero.exists())
            throw new PersistenciaException("No se encontró el fichero: " + rutaFichero);

        try (BufferedReader br = new BufferedReader(new FileReader(fichero))) {
            String linea;
            boolean primera = true;
            while ((linea = br.readLine()) != null) {
                if (primera) { primera = false; continue; } // saltar cabecera
                if (!linea.isBlank()) lineas.add(linea);
            }
        } catch (IOException e) {
            Logger.error("Error al leer CSV: " + e.getMessage());
            throw new PersistenciaException("No se pudo leer el fichero CSV.", e);
        }

        return lineas;
    }
}