package com.dacarex.capital.enums;

public enum Tema {
    CLARO("Claro"),
    OSCURO("Oscuro");

    private final String valor;

    Tema(String valor) { this.valor = valor; }

    public String getValor() { return valor; }

    @Override
    public String toString() { return valor; }
}