package com.dacarex.capital.ui;

import java.util.ArrayList;
import java.util.List;

public class EstadoTabla<T> {

    private List<T> datos;
    private boolean cargando;
    private boolean sinResultados;
    private Paginacion paginacion;

    public EstadoTabla() {
        this.datos = new ArrayList<>();
        this.cargando = false;
        this.sinResultados = true;
        this.paginacion = new Paginacion();
    }

    public EstadoTabla(List<T> datos, Paginacion paginacion) {
        this.datos = datos != null ? datos : new ArrayList<>();
        this.cargando = false;
        this.sinResultados = this.datos.isEmpty();
        this.paginacion = paginacion != null ? paginacion : new Paginacion();
    }

    public void actualizar(List<T> datos, int totalElementos) {
        this.datos = datos != null ? datos : new ArrayList<>();
        this.cargando = false;
        this.sinResultados = this.datos.isEmpty();
        this.paginacion.setTotalElementos(totalElementos);
    }

    public void setCargando(boolean cargando) {
        this.cargando = cargando;
    }

    // Getters
    public List<T> getDatos() { return datos; }
    public boolean isCargando() { return cargando; }
    public boolean isSinResultados() { return sinResultados; }
    public Paginacion getPaginacion() { return paginacion; }
    public void setPaginacion(Paginacion paginacion) { this.paginacion = paginacion; }
}