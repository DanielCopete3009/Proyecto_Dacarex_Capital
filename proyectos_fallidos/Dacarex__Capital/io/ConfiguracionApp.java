package com.dacarex.capital.io;

import com.dacarex.capital.exception.PersistenciaException;

import java.io.*;
import java.util.Properties;

public class ConfiguracionApp {

    private static final String RUTA = "config.properties";
    private final Properties props = new Properties();

    public ConfiguracionApp() {
        cargar();
    }

    private void cargar() {
        File fichero = new File(RUTA);

        // Si no existe, creamos uno con valores por defecto
        if (!fichero.exists()) {
            crearPorDefecto();
        }

        try (InputStream is = new FileInputStream(fichero)) {
            props.load(is);
            Logger.info("Configuración cargada correctamente.");
        } catch (IOException e) {
            Logger.error("Error al cargar configuración: " + e.getMessage());
            throw new PersistenciaException("No se pudo cargar config.properties", e);
        }
    }

    private void crearPorDefecto() {
        Properties defecto = new Properties();
        defecto.setProperty("db.url",      "jdbc:mysql://localhost:3306/dacarex_capital");
        defecto.setProperty("db.usuario",  "root");
        defecto.setProperty("db.password", "");
        defecto.setProperty("app.nombre",  "Dacarex Capital");
        defecto.setProperty("app.version", "1.0.0");
        defecto.setProperty("app.moneda",  "EUR");

        try (OutputStream os = new FileOutputStream(RUTA)) {
            defecto.store(os, "Configuracion Dacarex Capital");
            Logger.info("Fichero config.properties creado con valores por defecto.");
        } catch (IOException e) {
            Logger.error("No se pudo crear config.properties: " + e.getMessage());
        }
    }

    public void guardar() {
        try (OutputStream os = new FileOutputStream(RUTA)) {
            props.store(os, "Configuracion Dacarex Capital");
            Logger.info("Configuración guardada correctamente.");
        } catch (IOException e) {
            Logger.error("Error al guardar configuración: " + e.getMessage());
            throw new PersistenciaException("No se pudo guardar config.properties", e);
        }
    }

    // Getters de configuración
    public String getDbUrl()      { return props.getProperty("db.url",      "jdbc:mysql://localhost:3306/dacarex_capital"); }
    public String getDbUsuario()  { return props.getProperty("db.usuario",  "root"); }
    public String getDbPassword() { return props.getProperty("db.password", ""); }
    public String getAppNombre()  { return props.getProperty("app.nombre",  "Dacarex Capital"); }
    public String getAppVersion() { return props.getProperty("app.version", "1.0.0"); }
    public String getAppMoneda()  { return props.getProperty("app.moneda",  "EUR"); }

    // Setters de configuración
    public void setDbUrl(String url)          { props.setProperty("db.url", url); }
    public void setDbUsuario(String usuario)  { props.setProperty("db.usuario", usuario); }
    public void setDbPassword(String pass)    { props.setProperty("db.password", pass); }
    public void setAppMoneda(String moneda)   { props.setProperty("app.moneda", moneda); }
}