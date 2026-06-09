package com.dacarex.capital.dao;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;

public class ConexionDB {

    // Variables privadas para el patrón Singleton y la gestión de JPA/ObjectDB
    private static ConexionDB instancia;
    private EntityManagerFactory emf;
    private EntityManager em;

    // Constructor privado: evita que se creen instancias con "new ConexionDB()"
    private ConexionDB() {
        try {
            // 1. Apunta a la ubicación del archivo de la base de datos (.odb)
            emf = Persistence.createEntityManagerFactory(
                "$objectdb/db/dacarex.odb"
            );
            // 2. Abre el gestor de entidades para realizar operaciones (buscar, guardar, etc.)
            em = emf.createEntityManager();
            System.out.println("Conexion a ObjectDB establecida.");
        } catch (Exception e) {
            System.out.println("Error al conectar con ObjectDB: " + e.getMessage());
        }
    }

    // Método Singleton: asegura que todo el programa use la misma y única conexión
    public static ConexionDB getInstance() {
        if (instancia == null) instancia = new ConexionDB();
        return instancia;
    }

    // Devuelve el EntityManager para que los DAOs puedan hacer consultas
    public EntityManager getEm() { return em; }

    // Cierra el gestor y la fábrica de conexiones de forma limpia al salir del programa
    public void cerrar() {
        if (em != null && em.isOpen()) em.close();
        if (emf != null && emf.isOpen()) emf.close();
        System.out.println("Conexion cerrada.");
    }
}