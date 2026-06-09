package com.dacarex.capital.model;

import com.dacarex.capital.enums.TipoMovimiento;

import java.util.ArrayList;
import java.util.List;

public class Categoria extends EntidadBase implements IValidable {

    private String nombre;
    private TipoMovimiento tipo;
    private boolean enUso;

    public Categoria(String id, String nombre, TipoMovimiento tipo) {
        super(id);
        this.nombre = nombre;
        this.tipo = tipo;
        this.enUso = false;
    }

    public Categoria(String id, String nombre, TipoMovimiento tipo, boolean enUso) {
        super(id);
        this.nombre = nombre;
        this.tipo = tipo;
        this.enUso = enUso;
    }

    @Override
    public List<String> validar() {
        List<String> errores = new ArrayList<>();
        if (nombre == null || nombre.isBlank())
            errores.add("El nombre de la categoría es obligatorio.");
        if (tipo == null)
            errores.add("El tipo de categoría es obligatorio.");
        return errores;
    }

    @Override
    public String toResumen() {
        return new StringBuilder("Categoria{")
                .append("id=").append(getId())
                .append(", nombre=").append(nombre)
                .append(", tipo=").append(tipo)
                .append("}").toString();
    }

    // Getters y Setters
    public String getNombre() { return nombre; }
    public void setNombre(String nombre) { this.nombre = nombre; }

    public TipoMovimiento getTipo() { return tipo; }
    public void setTipo(TipoMovimiento tipo) { this.tipo = tipo; }

    public boolean isEnUso() { return enUso; }
    public void setEnUso(boolean enUso) { this.enUso = enUso; }
}