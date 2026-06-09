package com.dacarex.capital;

import com.dacarex.capital.dao.ConexionBD;
import com.dacarex.capital.vista.VentanaLogin;

import javax.swing.*;

public class Main {

    public static void main(String[] args) {

        if (!ConexionBD.getInstance().estaConectado()) {
            JOptionPane.showMessageDialog(null,
                "No se pudo conectar a MySQL.\n" +
                "Comprueba que MySQL esta arrancado\n" +
                "y que la base de datos 'dacarex_capital' existe.",
                "Error de conexion",
                JOptionPane.WARNING_MESSAGE);
        }

        SwingUtilities.invokeLater(() -> {
            try {
                // Nimbus respeta los colores personalizados
                UIManager.setLookAndFeel(
                    "javax.swing.plaf.nimbus.NimbusLookAndFeel"
                );
            } catch (Exception e) {
                // usar por defecto si falla
            }
            new VentanaLogin().setVisible(true);
        });
    }
}