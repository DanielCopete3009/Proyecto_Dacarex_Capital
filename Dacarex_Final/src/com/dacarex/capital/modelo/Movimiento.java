package com.dacarex.capital.modelo;

import javax.persistence.*;
import java.time.LocalDate;

@Entity
public class Movimiento extends EntidadBase {

    @Enumerated(EnumType.STRING)
    private TipoMovimiento tipo;

    private String descripcion;
    private double importe;
    private LocalDate fecha;
    private String notas;

    @ManyToOne
    private Categoria categoria;

    public Movimiento() {}

    public Movimiento(TipoMovimiento tipo, String descripcion, double importe,
                      Categoria categoria, LocalDate fecha, String notas) {
        this.tipo        = tipo;
        this.descripcion = descripcion;
        this.importe     = importe;
        this.categoria   = categoria;
        this.fecha       = fecha;
        this.notas       = notas;
    }

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
    public String toResumen() {
        return fecha + " | " + tipo + " | " + descripcion + " | " + importe + "€";
    }

    // Getters y Setters
    public TipoMovimiento getTipo() { return tipo; }
    public void setTipo(TipoMovimiento tipo) { this.tipo = tipo; }

    public String getDescripcion() { return descripcion; }
    public void setDescripcion(String d) { this.descripcion = d; }

    public double getImporte() { return importe; }
    public void setImporte(double importe) { this.importe = importe; }

    public LocalDate getFecha() { return fecha; }
    public void setFecha(LocalDate fecha) { this.fecha = fecha; }

    public String getNotas() { return notas; }
    public void setNotas(String notas) { this.notas = notas; }

    public Categoria getCategoria() { return categoria; }
    public void setCategoria(Categoria categoria) { this.categoria = categoria; }
}