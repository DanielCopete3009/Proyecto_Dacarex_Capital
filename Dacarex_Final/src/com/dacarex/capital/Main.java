package com.dacarex.capital;

import com.dacarex.capital.dao.CategoriaDAO;
import com.dacarex.capital.dao.ConexionDB;
import com.dacarex.capital.dao.UsuarioDAO;
import com.dacarex.capital.vista.VentanaLogin;

public class Main {

    public static void main(String[] args) {

        System.out.println("=== DACAREX CAPITAL ===");

        // 1. Abre la conexión con la base de datos ObjectDB
        ConexionDB.getInstance();

        // 2. Crea las categorías por defecto si la base de datos está vacía
        CategoriaDAO categoriaDAO = new CategoriaDAO();
        categoriaDAO.cargarIniciales();

        // 3. Crea un usuario de prueba para poder entrar la primera vez
        UsuarioDAO usuarioDAO = new UsuarioDAO();
        usuarioDAO.crearUsuarioDemoSiVacio();

        System.out.println("Datos iniciales listos.");

        // 4. Muestra la ventana de Login en la pantalla
        VentanaLogin login = new VentanaLogin();
        login.setVisible(true);
    }
}