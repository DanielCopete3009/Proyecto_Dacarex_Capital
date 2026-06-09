package com.dacarex.capital.dashboard;

public class PuntoGrafico {

    private String etiqueta;
    private double ingresos;
    private double gastos;
    private double flujoDeCaja;

    public PuntoGrafico(String etiqueta, double ingresos, double gastos) {
        this.etiqueta = etiqueta;
        this.ingresos = ingresos;
        this.gastos = gastos;
        this.flujoDeCaja = ingresos - gastos;
    }

    // Getters
    public String getEtiqueta() { return etiqueta; }
    public double getIngresos() { return ingresos; }
    public double getGastos() { return gastos; }
    public double getFlujoDeCaja() { return flujoDeCaja; }

    @Override
    public String toString() {
        return new StringBuilder("PuntoGrafico{")
                .append("etiqueta=").append(etiqueta)
                .append(", ingresos=").append(ingresos)
                .append(", gastos=").append(gastos)
                .append(", flujo=").append(flujoDeCaja)
                .append("}").toString();
    }
}