package com.dacarex.capital.modelo;

import com.dacarex.capital.enums.TipoMovimiento;

public class Categoria {

    private int id;
    private String nombre;
    private TipoMovimiento tipo;

    public Categoria(int id, String nombre, TipoMovimiento tipo) {
        this.id = id;
        this.nombre = nombre;
        this.tipo = tipo;
    }

    // Getters y Setters
    public int getId() { return id; }
    public void setId(int id) { this.id = id; }

    public String getNombre() { return nombre; }
    public void setNombre(String nombre) { this.nombre = nombre; }

    public TipoMovimiento getTipo() { return tipo; }
    public void setTipo(TipoMovimiento tipo) { this.tipo = tipo; }

    @Override
    public String toString() {
        return new StringBuilder("Categoria{")
                .append("id=").append(id)
                .append(", nombre=").append(nombre)
                .append(", tipo=").append(tipo)
                .append("}").toString();
    }
}