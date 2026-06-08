package com.dacarex.capital.model;

import com.dacarex.capital.enums.TipoCuenta;

public class Usuario extends EntidadBase {

    private String nombreCompleto;
    private String email;
    private String contrasenia;
    private TipoCuenta tipoCuenta;
    private String nombreEmpresa;
    private PreferenciasUsuario preferencias;

    public Usuario(String id, String nombreCompleto, String email, String contrasenia,
                   TipoCuenta tipoCuenta, String nombreEmpresa, PreferenciasUsuario preferencias) {
        super(id);
        this.nombreCompleto = nombreCompleto;
        this.email = email;
        this.contrasenia = contrasenia;
        this.tipoCuenta = tipoCuenta;
        this.nombreEmpresa = nombreEmpresa;
        this.preferencias = preferencias != null ? preferencias : new PreferenciasUsuario();
    }

    @Override
    public String toResumen() {
        return new StringBuilder("Usuario{")
                .append("id=").append(getId())
                .append(", nombre=").append(nombreCompleto)
                .append(", email=").append(email)
                .append(", tipo=").append(tipoCuenta)
                .append("}").toString();
    }

    // Getters y Setters
    public String getNombreCompleto() { return nombreCompleto; }
    public void setNombreCompleto(String nombreCompleto) { this.nombreCompleto = nombreCompleto; }

    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }

    public String getContrasenia() { return contrasenia; }
    public void setContrasenia(String contrasenia) { this.contrasenia = contrasenia; }

    public TipoCuenta getTipoCuenta() { return tipoCuenta; }
    public void setTipoCuenta(TipoCuenta tipoCuenta) { this.tipoCuenta = tipoCuenta; }

    public String getNombreEmpresa() { return nombreEmpresa; }
    public void setNombreEmpresa(String nombreEmpresa) { this.nombreEmpresa = nombreEmpresa; }

    public PreferenciasUsuario getPreferencias() { return preferencias; }
    public void setPreferencias(PreferenciasUsuario preferencias) { this.preferencias = preferencias; }
}