package com.dacarex.capital.informe;

public class DistribucionGasto {

    private String categoria;
    private double importe;
    private double porcentaje;

    public DistribucionGasto(String categoria, double importe, double total) {
        this.categoria = categoria;
        this.importe = importe;
        this.porcentaje = total > 0 ? (importe / total) * 100 : 0;
    }

    // Getters
    public String getCategoria() { return categoria; }
    public double getImporte() { return importe; }
    public double getPorcentaje() { return porcentaje; }

    @Override
    public String toString() {
        return new StringBuilder("DistribucionGasto{")
                .append("categoria=").append(categoria)
                .append(", importe=").append(importe)
                .append(", porcentaje=").append(String.format("%.1f", porcentaje)).append("%")
                .append("}").toString();
    }
}