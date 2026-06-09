package com.dacarex.capital.db;

import com.dacarex.capital.exception.PersistenciaException;
import com.dacarex.capital.io.ConfiguracionApp;
import com.dacarex.capital.io.Logger;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class ConexionBD {

    private static ConexionBD instancia;
    private Connection conexion;
    private final ConfiguracionApp config;

    private ConexionBD() {
        this.config = new ConfiguracionApp();
        conectar();
    }

    public static ConexionBD getInstance() {
        if (instancia == null) {
            instancia = new ConexionBD();
        }
        return instancia;
    }

    private void conectar() {
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            conexion = DriverManager.getConnection(
                config.getDbUrl(),
                config.getDbUsuario(),
                config.getDbPassword()
            );
            Logger.info("Conexión a base de datos establecida correctamente.");
        } catch (ClassNotFoundException e) {
            Logger.error("Driver MySQL no encontrado: " + e.getMessage());
            throw new PersistenciaException("Driver MySQL no encontrado.", e);
        } catch (SQLException e) {
            Logger.error("Error al conectar con la BD: " + e.getMessage());
            throw new PersistenciaException("No se pudo conectar a la base de datos.", e);
        }
    }

    public Connection getConexion() {
        try {
            if (conexion == null || conexion.isClosed()) {
                Logger.advertencia("Conexión cerrada. Reconectando...");
                conectar();
            }
        } catch (SQLException e) {
            throw new PersistenciaException("Error al verificar la conexión.", e);
        }
        return conexion;
    }

    public void cerrar() {
        try {
            if (conexion != null && !conexion.isClosed()) {
                conexion.close();
                Logger.info("Conexión a base de datos cerrada.");
            }
        } catch (SQLException e) {
            Logger.error("Error al cerrar la conexión: " + e.getMessage());
        }
    }

    public boolean estaConectado() {
        try {
            return conexion != null && !conexion.isClosed();
        } catch (SQLException e) {
            return false;
        }
    }
}