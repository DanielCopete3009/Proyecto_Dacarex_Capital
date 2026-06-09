package com.dacarex.capital.enums;

public enum PeriodoInforme {
    MENSUAL("Mensual"),
    TRIMESTRAL("Trimestral"),
    ANUAL("Anual"),
    PERSONALIZADO("Personalizado");

    private final String valor;

    PeriodoInforme(String valor) { this.valor = valor; }

    public String getValor() { return valor; }

    @Override
    public String toString() { return valor; }
}