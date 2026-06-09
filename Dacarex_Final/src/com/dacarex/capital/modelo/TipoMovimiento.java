package com.dacarex.capital.modelo;

// Un enum define un conjunto fijo de constantes, limitando los valores posibles a solo estos dos
public enum TipoMovimiento {
    // Definición de las constantes con su texto legible asociado
    INGRESO("Ingreso"),
    GASTO("Gasto");

    // Atributo final para almacenar el texto limpio de la constante
    private final String valor;

    // Constructor interno: asigna el texto a la constante (ej: INGRESO se asocia con "Ingreso")
    TipoMovimiento(String valor) { this.valor = valor; }

    // Método para recuperar el texto limpio (útil para mostrar en componentes de la interfaz)
    public String getValor() { return valor; }

    // Sobreescribe el método estándar para que al imprimir el enum devuelva directamente su texto legible
    @Override
    public String toString() { return valor; }
}