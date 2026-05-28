package com.dacarex.capital.model;

import com.dacarex.capital.enums.TipoMovimiento;

import java.time.LocalDate;

public class FiltrosMovimientos {

    private String textoBusqueda;
    private LocalDate fechaDesde;
    private LocalDate fechaHasta;
    private String categoriaId;
    private TipoMovimiento tipo;

    public FiltrosMovimientos() {
        limpiar();
    }

    public FiltrosMovimientos(String textoBusqueda, LocalDate fechaDesde, LocalDate fechaHasta,
                              String categoriaId, TipoMovimiento tipo) {
        this.textoBusqueda = textoBusqueda;
        this.fechaDesde = fechaDesde;
        this.fechaHasta = fechaHasta;
        this.categoriaId = categoriaId;
        this.tipo = tipo;
    }

    public void limpiar() {
        this.textoBusqueda = "";
        this.fechaDesde = null;
        this.fechaHasta = null;
        this.categoriaId = null;
        this.tipo = null;
    }

    public boolean estaActivo() {
        return (textoBusqueda != null && !textoBusqueda.isBlank())
                || fechaDesde != null
                || fechaHasta != null
                || categoriaId != null
                || tipo != null;
    }

    // Getters y Setters
    public String getTextoBusqueda() { return textoBusqueda; }
    public void setTextoBusqueda(String textoBusqueda) { this.textoBusqueda = textoBusqueda; }

    public LocalDate getFechaDesde() { return fechaDesde; }
    public void setFechaDesde(LocalDate fechaDesde) { this.fechaDesde = fechaDesde; }

    public LocalDate getFechaHasta() { return fechaHasta; }
    public void setFechaHasta(LocalDate fechaHasta) { this.fechaHasta = fechaHasta; }

    public String getCategoriaId() { return categoriaId; }
    public void setCategoriaId(String categoriaId) { this.categoriaId = categoriaId; }

    public TipoMovimiento getTipo() { return tipo; }
    public void setTipo(TipoMovimiento tipo) { this.tipo = tipo; }
}