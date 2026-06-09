package com.dacarex.capital;

import com.dacarex.capital.dao.CategoriaDAO;
import com.dacarex.capital.dao.ConexionDB;
import com.dacarex.capital.dao.UsuarioDAO;
import com.dacarex.capital.vista.VentanaLogin;

public class Main {

    public static void main(String[] args) {

        System.out.println("=== DACAREX CAPITAL ===");

        // Inicializar conexión ObjectDB
        ConexionDB.getInstance();

        // Cargar datos iniciales si la BD está vacía
        CategoriaDAO categoriaDAO = new CategoriaDAO();
        categoriaDAO.cargarIniciales();

        UsuarioDAO usuarioDAO = new UsuarioDAO();
        usuarioDAO.crearUsuarioDemoSiVacio();

        System.out.println("Datos iniciales listos.");

        // Lanzar interfaz gráfica
        VentanaLogin login = new VentanaLogin();
        login.setVisible(true);
        
       
    }
}