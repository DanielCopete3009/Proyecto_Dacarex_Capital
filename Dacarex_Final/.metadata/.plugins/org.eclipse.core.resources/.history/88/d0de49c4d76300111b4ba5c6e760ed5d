package com.dacarex.capital.modelo;

import javax.persistence.*;

@Entity
public class Usuario extends EntidadBase {

    private String nombre;
    private String email;
    private String contrasenia;

    public Usuario() {}

    public Usuario(String nombre, String email, String contrasenia) {
        this.nombre      = nombre;
        this.email       = email;
        this.contrasenia = contrasenia;
    }

    @Override
    public String toResumen() {
        return nombre + " (" + email + ")";
    }

    // Getters y Setters
    public String getNombre() { return nombre; }
    public void setNombre(String nombre) { this.nombre = nombre; }

    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }

    public String getContrasenia() { return contrasenia; }
    public void setContrasenia(String contrasenia) { this.contrasenia = contrasenia; }
}