package com.dacarex.capital.auth;

import com.dacarex.capital.model.IValidable;

import java.util.ArrayList;
import java.util.List;

public class CredencialesLogin implements IValidable {

    private String usuarioOEmail;
    private String contrasenia;

    public CredencialesLogin(String usuarioOEmail, String contrasenia) {
        this.usuarioOEmail = usuarioOEmail;
        this.contrasenia = contrasenia;
    }

    @Override
    public List<String> validar() {
        List<String> errores = new ArrayList<>();
        if (usuarioOEmail == null || usuarioOEmail.isBlank())
            errores.add("El usuario o email es obligatorio.");
        if (contrasenia == null || contrasenia.isEmpty())
            errores.add("La contraseña es obligatoria.");
        return errores;
    }

    // Getters y Setters
    public String getUsuarioOEmail() { return usuarioOEmail; }
    public void setUsuarioOEmail(String usuarioOEmail) { this.usuarioOEmail = usuarioOEmail; }

    public String getContrasenia() { return contrasenia; }
    public void setContrasenia(String contrasenia) { this.contrasenia = contrasenia; }
}