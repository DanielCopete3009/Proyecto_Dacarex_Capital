package com.dacarex.capital.informe;

public class KPIsInforme {

    private double saldoAcumulado;
    private double mayorIngresoRegistrado;
    private double mayorGastoRegistrado;
    private String categoriaConMasMovimientos;

    public KPIsInforme(double saldoAcumulado, double mayorIngresoRegistrado,
                       double mayorGastoRegistrado, String categoriaConMasMovimientos) {
        this.saldoAcumulado = saldoAcumulado;
        this.mayorIngresoRegistrado = mayorIngresoRegistrado;
        this.mayorGastoRegistrado = mayorGastoRegistrado;
        this.categoriaConMasMovimientos = categoriaConMasMovimientos;
    }

    // Getters
    public double getSaldoAcumulado() { return saldoAcumulado; }
    public double getMayorIngresoRegistrado() { return mayorIngresoRegistrado; }
    public double getMayorGastoRegistrado() { return mayorGastoRegistrado; }
    public String getCategoriaConMasMovimientos() { return categoriaConMasMovimientos; }

    @Override
    public String toString() {
        return new StringBuilder("KPIsInforme{")
                .append("saldoAcumulado=").append(saldoAcumulado)
                .append(", mayorIngreso=").append(mayorIngresoRegistrado)
                .append(", mayorGasto=").append(mayorGastoRegistrado)
                .append(", catMasActiva=").append(categoriaConMasMovimientos)
                .append("}").toString();
    }
}