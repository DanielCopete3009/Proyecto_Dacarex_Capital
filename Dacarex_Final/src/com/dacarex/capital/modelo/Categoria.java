package com.dacarex.capital.modelo;

import javax.persistence.*;

// @Entity le dice a ObjectDB/JPA que esta clase se convertirá en una tabla de la base de datos
@Entity
public class Categoria extends EntidadBase { // Hereda de EntidadBase (seguramente contiene el ID único)

    private String nombre;

    // Guarda el valor del Enum en la base de datos como un texto ("INGRESO" o "GASTO") en lugar de un número
    @Enumerated(EnumType.STRING)
    private TipoMovimiento tipo;

    // Constructor vacío obligatorio: JPA lo necesita para poder recuperar los datos de la BD
    public Categoria() {}

    // Constructor cómodo para crear una categoría con datos rápidamente
    public Categoria(String nombre, TipoMovimiento tipo) {
        this.nombre = nombre;
        this.tipo   = tipo;
    }

    // Método sobreescrito para devolver un texto rápido y legible del objeto (ej: "Nomina (INGRESO)")
    @Override
    public String toResumen() {
        return nombre + " (" + tipo + ")";
    }

    // ── GETTERS Y SETTERS (Métodos para leer y modificar las variables privadas) ──

    public String getNombre() { return nombre; }
    public void setNombre(String nombre) { this.nombre = nombre; }

    public TipoMovimiento getTipo() { return tipo; }
    public void setTipo(TipoMovimiento tipo) { this.tipo = tipo; }
}