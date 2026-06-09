package com.dacarex.capital.modelo;

import javax.persistence.*;

@Entity
public class Categoria extends EntidadBase {

    private String nombre;

    @Enumerated(EnumType.STRING)
    private TipoMovimiento tipo;

    public Categoria() {}

    public Categoria(String nombre, TipoMovimiento tipo) {
        this.nombre = nombre;
        this.tipo   = tipo;
    }

    @Override
    public String toResumen() {
        return nombre + " (" + tipo + ")";
    }

    public String getNombre() { return nombre; }
    public void setNombre(String nombre) { this.nombre = nombre; }

    public TipoMovimiento getTipo() { return tipo; }
    public void setTipo(TipoMovimiento tipo) { this.tipo = tipo; }
}