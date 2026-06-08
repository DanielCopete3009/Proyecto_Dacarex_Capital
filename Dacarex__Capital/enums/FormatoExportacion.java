package com.dacarex.capital.enums;

public enum FormatoExportacion {
    CSV("CSV"),
    PDF("PDF"),
    EXCEL("Excel");

    private final String valor;

    FormatoExportacion(String valor) { this.valor = valor; }

    public String getValor() { return valor; }

    @Override
    public String toString() { return valor; }
}