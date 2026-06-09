package com.dacarex.capital.model;

import com.dacarex.capital.enums.TipoMovimiento;

import java.time.LocalDate;

public class Movimiento extends EntidadBase implements IExportable {

    private TipoMovimiento tipo;
    private String descripcion;
    private double importe;
    private Categoria categoria;
    private LocalDate fecha;
    private String notas;
    private double saldoResultante;

    public Movimiento(String id, TipoMovimiento tipo, String descripcion, double importe,
                      Categoria categoria, LocalDate fecha, double saldoResultante, String notas) {
        super(id);
        this.tipo = tipo;
        this.descripcion = descripcion;
        this.importe = importe;
        this.categoria = categoria;
        this.fecha = fecha;
        this.saldoResultante = saldoResultante;
        this.notas = notas;
    }

    @Override
    public String toCsv() {
        return new StringBuilder()
                .append(fecha).append(",")
                .append(tipo).append(",")
                .append(descripcion).append(",")
                .append(importe).append(",")
                .append(categoria != null ? categoria.getNombre() : "").append(",")
                .append(notas != null ? notas : "")
                .toString();
    }

    @Override
    public String toTexto() {
        return new StringBuilder("Movimiento{")
                .append("fecha=").append(fecha)
                .append(", tipo=").append(tipo)
                .append(", descripcion=").append(descripcion)
                .append(", importe=").append(importe).append("€")
                .append(", categoria=").append(categoria != null ? categoria.getNombre() : "-")
                .append("}").toString();
    }

    @Override
    public String toResumen() {
        return toTexto();
    }

    // Getters y Setters
    public TipoMovimiento getTipo() { return tipo; }
    public void setTipo(TipoMovimiento tipo) { this.tipo = tipo; }

    public String getDescripcion() { return descripcion; }
    public void setDescripcion(String descripcion) { this.descripcion = descripcion; }

    public double getImporte() { return importe; }
    public void setImporte(double importe) { this.importe = importe; }

    public Categoria getCategoria() { return categoria; }
    public void setCategoria(Categoria categoria) { this.categoria = categoria; }

    public LocalDate getFecha() { return fecha; }
    public void setFecha(LocalDate fecha) { this.fecha = fecha; }

    public String getNotas() { return notas; }
    public void setNotas(String notas) { this.notas = notas; }

    public double getSaldoResultante() { return saldoResultante; }
    public void setSaldoResultante(double saldoResultante) { this.saldoResultante = saldoResultante; }
}