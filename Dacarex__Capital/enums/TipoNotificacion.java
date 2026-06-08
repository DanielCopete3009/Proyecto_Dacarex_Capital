package com.dacarex.capital.enums;

public enum TipoNotificacion {
    EXITO("Éxito"),
    ADVERTENCIA("Advertencia"),
    ERROR("Error"),
    INFORMACION("Información");

    private final String valor;

    TipoNotificacion(String valor) { this.valor = valor; }

    public String getValor() { return valor; }

    @Override
    public String toString() { return valor; }
}
