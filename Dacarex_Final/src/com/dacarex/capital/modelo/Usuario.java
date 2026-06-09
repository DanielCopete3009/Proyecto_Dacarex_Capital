package com.dacarex.capital.modelo;

import javax.persistence.*;

// @Entity le dice a ObjectDB/JPA que esta clase se convertirá en una tabla de la base de datos
@Entity
public class Usuario extends EntidadBase { // Hereda el ID único y autogenerado de EntidadBase

    private String nombre;
    private String email;
    private String contrasenia;

    // Constructor vacío obligatorio: Requerido por JPA para reconstruir el usuario al leer la BD
    public Usuario() {}

    // Constructor completo para instanciar un usuario con sus datos rápidamente
    public Usuario(String nombre, String email, String contrasenia) {
        this.nombre      = nombre;
        this.email       = email;
        this.contrasenia = contrasenia;
    }

    // Cumple con la obligación de EntidadBase y devuelve una vista rápida (ej: "Demo (demo@dacarex.com)")
    @Override
    public String toResumen() {
        return nombre + " (" + email + ")";
    }

    // ── GETTERS Y SETTERS (Métodos de acceso para leer y modificar las variables privadas) ──

    public String getNombre() { return nombre; }
    public void setNombre(String nombre) { this.nombre = nombre; }

    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }

    public String getContrasenia() { return contrasenia; }
    public void setContrasenia(String contrasenia) { this.contrasenia = contrasenia; }
}