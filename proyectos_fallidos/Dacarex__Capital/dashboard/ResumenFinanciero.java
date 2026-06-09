package com.dacarex.capital.dashboard;

import com.dacarex.capital.enums.PeriodoDashboard;

public class ResumenFinanciero {

    private double saldoDisponible;
    private double totalIngresos;
    private double totalGastos;
    private double flujoDeCaja;
    private PeriodoDashboard periodo;
    private String estadoFinanciero;

    public ResumenFinanciero(double saldoDisponible, double totalIngresos,
                             double totalGastos, PeriodoDashboard periodo) {
        this.saldoDisponible = saldoDisponible;
        this.totalIngresos = totalIngresos;
        this.totalGastos = totalGastos;
        this.flujoDeCaja = totalIngresos - totalGastos;
        this.periodo = periodo;
        this.estadoFinanciero = flujoDeCaja > 0 ? "positivo"
                              : flujoDeCaja < 0 ? "negativo"
                              : "neutro";
    }

    // Getters
    public double getSaldoDisponible() { return saldoDisponible; }
    public double getTotalIngresos() { return totalIngresos; }
    public double getTotalGastos() { return totalGastos; }
    public double getFlujoDeCaja() { return flujoDeCaja; }
    public PeriodoDashboard getPeriodo() { return periodo; }
    public String getEstadoFinanciero() { return estadoFinanciero; }

    @Override
    public String toString() {
        return new StringBuilder("ResumenFinanciero{")
                .append("saldo=").append(saldoDisponible)
                .append(", ingresos=").append(totalIngresos)
                .append(", gastos=").append(totalGastos)
                .append(", flujo=").append(flujoDeCaja)
                .append(", estado=").append(estadoFinanciero)
                .append(", periodo=").append(periodo)
                .append("}").toString();
    }
}