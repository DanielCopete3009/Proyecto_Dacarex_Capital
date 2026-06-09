package com.dacarex.capital.modelo;

import javax.persistence.*;
import java.time.LocalDate;

// @Entity le dice a ObjectDB/JPA que esta clase se mapeará como una tabla en la base de datos
@Entity
public class Movimiento extends EntidadBase { // Hereda el ID único de EntidadBase

    // Almacena el Enum en la base de datos como una cadena de texto ("INGRESO" o "GASTO")
    @Enumerated(EnumType.STRING)
    private TipoMovimiento tipo;

    private String descripcion;
    private double importe;
    private LocalDate fecha;
    private String notas;

    // Relación de base de datos: Muchos movimientos pueden tener una misma Categoría
    @ManyToOne
    private Categoria categoria;

    // Constructor vacío obligatorio: Requerido por JPA para poder reconstruir el objeto desde la BD
    public Movimiento() {}

    // Constructor completo para instanciar un movimiento con todos sus datos rápidamente
    public Movimiento(TipoMovimiento tipo, String descripcion, double importe,
                      Categoria categoria, LocalDate fecha, String notas) {
        this.tipo        = tipo;
        this.descripcion = descripcion;
        this.importe     = importe;
        this.categoria   = categoria;
        this.fecha       = fecha;
        this.notas       = notas;
    }

    // Convierte las propiedades del objeto en una línea de texto separada por comas para exportar a CSV/Excel
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

    // Cumple con la clase madre y devuelve una línea representativa del movimiento para las listas de la interfaz
    @Override
    public String toResumen() {
        return fecha + " | " + tipo + " | " + descripcion + " | " + importe + "€";
    }

    // ── GETTERS Y SETTERS (Métodos de acceso para leer y modificar los atributos privados) ──

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