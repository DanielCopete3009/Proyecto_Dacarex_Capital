package com.dacarex.capital.modelo;

import com.dacarex.capital.enums.TipoCuenta;

public class Usuario {

    private int id;
    private String nombre;
    private String email;
    private String contrasenia;
    private TipoCuenta tipoCuenta;

    public Usuario(int id, String nombre, String email,
                   String contrasenia, TipoCuenta tipoCuenta) {
        this.id = id;
        this.nombre = nombre;
        this.email = email;
        this.contrasenia = contrasenia;
        this.tipoCuenta = tipoCuenta;
    }

    // Getters y Setters
    public int getId() { return id; }
    public void setId(int id) { this.id = id; }

    public String getNombre() { return nombre; }
    public void setNombre(String nombre) { this.nombre = nombre; }

    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }

    public String getContrasenia() { return contrasenia; }
    public void setContrasenia(String contrasenia) { this.contrasenia = contrasenia; }

    public TipoCuenta getTipoCuenta() { return tipoCuenta; }
    public void setTipoCuenta(TipoCuenta tipoCuenta) { this.tipoCuenta = tipoCuenta; }

    @Override
    public String toString() {
        return new StringBuilder("Usuario{")
                .append("id=").append(id)
                .append(", nombre=").append(nombre)
                .append(", email=").append(email)
                .append(", tipo=").append(tipoCuenta)
                .append("}").toString();
    }
}