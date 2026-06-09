package com.dacarex.capital.informe;

import com.dacarex.capital.enums.PeriodoInforme;

import java.time.LocalDate;

public class ParametrosInforme {

    private PeriodoInforme periodo;
    private LocalDate fechaDesde;
    private LocalDate fechaHasta;

    public ParametrosInforme() {
        this.periodo = PeriodoInforme.MENSUAL;
    }

    public ParametrosInforme(PeriodoInforme periodo, LocalDate fechaDesde, LocalDate fechaHasta) {
        this.periodo = periodo;
        this.fechaDesde = fechaDesde;
        this.fechaHasta = fechaHasta;
    }

    public boolean esPersonalizado() {
        return PeriodoInforme.PERSONALIZADO.equals(periodo)
                && fechaDesde != null
                && fechaHasta != null;
    }

    // Getters y Setters
    public PeriodoInforme getPeriodo() { return periodo; }
    public void setPeriodo(PeriodoInforme periodo) { this.periodo = periodo; }

    public LocalDate getFechaDesde() { return fechaDesde; }
    public void setFechaDesde(LocalDate fechaDesde) { this.fechaDesde = fechaDesde; }

    public LocalDate getFechaHasta() { return fechaHasta; }
    public void setFechaHasta(LocalDate fechaHasta) { this.fechaHasta = fechaHasta; }

    @Override
    public String toString() {
        return new StringBuilder("ParametrosInforme{")
                .append("periodo=").append(periodo)
                .append(", desde=").append(fechaDesde)
                .append(", hasta=").append(fechaHasta)
                .append("}").toString();
    }
}