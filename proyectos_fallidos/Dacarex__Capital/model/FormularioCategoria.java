package com.dacarex.capital.model;

import com.dacarex.capital.enums.TipoMovimiento;

import java.util.ArrayList;
import java.util.List;

public class FormularioCategoria implements IValidable {

    private String nombre;
    private TipoMovimiento tipo;

    public FormularioCategoria() {
        this.nombre = "";
        this.tipo = TipoMovimiento.GASTO;
    }

    public FormularioCategoria(String nombre, TipoMovimiento tipo) {
        this.nombre = nombre;
        this.tipo = tipo;
    }

    @Override
    public List<String> validar() {
        List<String> errores = new ArrayList<>();
        if (nombre == null || nombre.isBlank())
            errores.add("El nombre de la categoría es obligatorio.");
        if (tipo == null)
            errores.add("El tipo es obligatorio.");
        return errores;
    }

    // Getters y Setters
    public String getNombre() { return nombre; }
    public void setNombre(String nombre) { this.nombre = nombre; }

    public TipoMovimiento getTipo() { return tipo; }
    public void setTipo(TipoMovimiento tipo) { this.tipo = tipo; }
}