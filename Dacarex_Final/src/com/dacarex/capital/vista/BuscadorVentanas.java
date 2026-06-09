package com.dacarex.capital.vista;

import java.awt.Component;
import java.awt.Frame;

public class BuscadorVentanas {

    // Este método recibe un componente cualquiera (un botón, una etiqueta) 
    // y busca hacia arriba hasta encontrar la ventana de la que depende.
    public static Frame getParentFrame(Component c) {
        
        // Mientras el componente exista y no hayamos llegado al final...
        while (c != null) {
            
            // ¿El componente actual es una Ventana Principal (Frame)?
            if (c instanceof Frame) {
                return (Frame) c; // ¡Encontrado! Lo convertimos a Frame y lo devolvemos.
            }
            
            // Si no lo es, subimos un escalón: agarramos al contenedor que lo contiene ("su padre")
            c = c.getParent();
        }
        
        // Si el bucle termina y no encontró ninguna ventana (caso raro), 
        // crea y devuelve una ventana vacía para que el programa no se rompa (no lance un NullPointerException).
        return new Frame();
    }
}