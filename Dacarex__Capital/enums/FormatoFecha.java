package com.dacarex.capital.enums;

public enum FormatoFecha {
    DD_MM_YYYY("DD/MM/YYYY"),
    MM_DD_YYYY("MM/DD/YYYY"),
    YYYY_MM_DD("YYYY-MM-DD");

    private final String valor;

    FormatoFecha(String valor) { this.valor = valor; }

    public String getValor() { return valor; }

    @Override
    public String toString() { return valor; }
}