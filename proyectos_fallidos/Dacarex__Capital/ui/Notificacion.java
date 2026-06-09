package com.dacarex.capital.ui;

import com.dacarex.capital.enums.TipoNotificacion;

public class Notificacion {

    private String id;
    private TipoNotificacion tipo;
    private String mensaje;
    private boolean visible;
    private int duracionMs;

    public Notificacion(String id, TipoNotificacion tipo, String mensaje) {
        this(id, tipo, mensaje, 4000);
    }

    public Notificacion(String id, TipoNotificacion tipo, String mensaje, int duracionMs) {
        this.id = id;
        this.tipo = tipo;
        this.mensaje = mensaje;
        this.visible = true;
        this.duracionMs = duracionMs;
    }

    public void ocultar() {
        this.visible = false;
    }

    // Getters y Setters
    public String getId() { return id; }
    public TipoNotificacion getTipo() { return tipo; }
    public String getMensaje() { return mensaje; }
    public boolean isVisible() { return visible; }
    public int getDuracionMs() { return duracionMs; }
    public void setDuracionMs(int duracionMs) { this.duracionMs = duracionMs; }

    @Override
    public String toString() {
        return new StringBuilder("Notificacion{")
                .append("tipo=").append(tipo)
                .append(", mensaje=").append(mensaje)
                .append(", visible=").append(visible)
                .append("}").toString();
    }
}