package com.dacarex.capital.auth;

import com.dacarex.capital.model.Usuario;

import java.time.LocalDateTime;

public class SesionUsuario {

    private Usuario usuario;
    private String token;
    private LocalDateTime expiraEn;

    public SesionUsuario(Usuario usuario, String token, LocalDateTime expiraEn) {
        this.usuario = usuario;
        this.token = token;
        this.expiraEn = expiraEn;
    }

    public boolean estaActiva() {
        return LocalDateTime.now().isBefore(expiraEn);
    }

    public void cerrar() {
        this.expiraEn = LocalDateTime.now().minusSeconds(1);
    }

    // Getters y Setters
    public Usuario getUsuario() { return usuario; }
    public void setUsuario(Usuario usuario) { this.usuario = usuario; }

    public String getToken() { return token; }
    public void setToken(String token) { this.token = token; }

    public LocalDateTime getExpiraEn() { return expiraEn; }
    public void setExpiraEn(LocalDateTime expiraEn) { this.expiraEn = expiraEn; }

    @Override
    public String toString() {
        return new StringBuilder("SesionUsuario{")
                .append("usuario=").append(usuario.getNombreCompleto())
                .append(", token=").append(token)
                .append(", activa=").append(estaActiva())
                .append("}").toString();
    }
}