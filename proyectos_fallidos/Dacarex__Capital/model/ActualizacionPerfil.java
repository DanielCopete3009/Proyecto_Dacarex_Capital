package com.dacarex.capital.model;

import java.util.ArrayList;
import java.util.List;

public class ActualizacionPerfil implements IValidable {

    private String nombreCompleto;
    private String email;
    private String nombreEmpresa;

    public ActualizacionPerfil(String nombreCompleto, String email, String nombreEmpresa) {
        this.nombreCompleto = nombreCompleto;
        this.email = email;
        this.nombreEmpresa = nombreEmpresa;
    }

    @Override
    public List<String> validar() {
        List<String> errores = new ArrayList<>();
        if (nombreCompleto == null || nombreCompleto.isBlank())
            errores.add("El nombre es obligatorio.");
        if (email == null || !email.contains("@"))
            errores.add("El email no es válido.");
        return errores;
    }

    // Getters y Setters
    public String getNombreCompleto() { return nombreCompleto; }
    public void setNombreCompleto(String nombreCompleto) { this.nombreCompleto = nombreCompleto; }

    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }

    public String getNombreEmpresa() { return nombreEmpresa; }
    public void setNombreEmpresa(String nombreEmpresa) { this.nombreEmpresa = nombreEmpresa; }
}