package com.dacarex.capital.util;

import com.dacarex.capital.modelo.Movimiento;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

public class ExportadorCSV {

    // Ruta de la carpeta donde se guardarán los archivos y la fila de títulos de Excel
    private static final String CARPETA  = "exportaciones/";
    private static final String CABECERA = "Fecha,Tipo,Descripcion,Importe,Categoria,Notas";

    // Recibe la lista de movimientos, los graba en el disco y devuelve la ruta del archivo creado
    public static String exportar(List<Movimiento> movimientos) throws IOException {

        // Crea la carpeta "exportaciones" en la raíz del proyecto si aún no existe
        new File(CARPETA).mkdirs();

        // Genera un nombre único usando la fecha y hora actual (ej: movimientos_20260609_153022.csv)
        String nombreFichero = CARPETA + "movimientos_"
                + LocalDateTime.now().format(
                    DateTimeFormatter.ofPattern("yyyyMMdd_HHmmss"))
                + ".csv";

        // Abre el archivo para escribir de forma segura (se cerrará solo al terminar el bloque try)
        try (BufferedWriter bw = new BufferedWriter(new FileWriter(nombreFichero))) {
            
            // 1. Escribe la fila con los títulos de las columnas
            bw.write(CABECERA);
            bw.newLine(); // Salto de línea
            
            // 2. Recorre la lista de movimientos y escribe cada uno en una fila nueva
            for (Movimiento m : movimientos) {
                bw.write(m.toCsv()); // Llama al método del modelo que separa los datos por comas
                bw.newLine();        // Salto de línea para el siguiente registro
            }
        }

        System.out.println("CSV exportado: " + nombreFichero);
        return nombreFichero; // Devuelve la ruta para poder mostrársela al usuario en la interfaz
    }
}