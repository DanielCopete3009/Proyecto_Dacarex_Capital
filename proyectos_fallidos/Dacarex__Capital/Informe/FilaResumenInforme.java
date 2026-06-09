package com.dacarex.capital.informe;

public class FilaResumenInforme {

    private String categoria;
    private double totalIngresos;
    private double totalGastos;
    private double diferencia;

    public FilaResumenInforme(String categoria, double totalIngresos, double totalGastos) {
        this.categoria = categoria;
        this.totalIngresos = totalIngresos;
        this.totalGastos = totalGastos;
        this.diferencia = totalIngresos - totalGastos;
    }

    // Getters
    public String getCategoria() { return categoria; }
    public double getTotalIngresos() { return totalIngresos; }
    public double getTotalGastos() { return totalGastos; }
    public double getDiferencia() { return diferencia; }

    @Override
    public String toString() {
        return new StringBuilder("FilaResumen{")
                .append("categoria=").append(categoria)
                .append(", ingresos=").append(totalIngresos)
                .append(", gastos=").append(totalGastos)
                .append(", diferencia=").append(diferencia)
                .append("}").toString();
    }
}