package com.dacarex.capital.dao;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class ConexionBD {

    private static final String URL      = "jdbc:mysql://localhost:3306/dacarex_capital";
    private static final String USUARIO  = "root";
    private static final String PASSWORD = "";

    private static ConexionBD instancia;
    private Connection conexion;

    private ConexionBD() {
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            conexion = DriverManager.getConnection(URL, USUARIO, PASSWORD);
            System.out.println(" Conexión a MySQL establecida.");
        } catch (ClassNotFoundException e) {
            System.out.println(" Driver MySQL no encontrado: " + e.getMessage());
        } catch (SQLException e) {
            System.out.println(" Error al conectar con MySQL: " + e.getMessage());
        }
    }

    public static ConexionBD getInstance() {
        if (instancia == null) instancia = new ConexionBD();
        return instancia;
    }

    public Connection getConexion() {
        try {
            if (conexion == null || conexion.isClosed()) {
                instancia = new ConexionBD();
            }
        } catch (SQLException e) {
            System.out.println(" Error al verificar conexión: " + e.getMessage());
        }
        return conexion;
    }

    public boolean estaConectado() {
        try {
            return conexion != null && !conexion.isClosed();
        } catch (SQLException e) {
            return false;
        }
    }

    public void cerrar() {
        try {
            if (conexion != null && !conexion.isClosed()) {
                conexion.close();
                System.out.println(" Conexión cerrada.");
            }
        } catch (SQLException e) {
            System.out.println(" Error al cerrar conexión: " + e.getMessage());
        }
    }
}