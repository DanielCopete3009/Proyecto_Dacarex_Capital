package com.dacarex.capital.ui;

public class Paginacion {

    private int paginaActual;
    private int elementosPorPagina;
    private int totalElementos;

    public Paginacion() {
        this(1, 20, 0);
    }

    public Paginacion(int paginaActual, int elementosPorPagina, int totalElementos) {
        this.paginaActual = paginaActual;
        this.elementosPorPagina = elementosPorPagina;
        this.totalElementos = totalElementos;
    }

    public int getTotalPaginas() {
        if (totalElementos == 0) return 1;
        return (int) Math.ceil((double) totalElementos / elementosPorPagina);
    }

    public int getOffset() {
        return (paginaActual - 1) * elementosPorPagina;
    }

    public void irAPagina(int pagina) {
        if (pagina >= 1 && pagina <= getTotalPaginas())
            this.paginaActual = pagina;
    }

    public void siguiente() {
        irAPagina(paginaActual + 1);
    }

    public void anterior() {
        irAPagina(paginaActual - 1);
    }

    public boolean hayAnterior() {
        return paginaActual > 1;
    }

    public boolean haySiguiente() {
        return paginaActual < getTotalPaginas();
    }

    // Getters y Setters
    public int getPaginaActual() { return paginaActual; }
    public void setPaginaActual(int paginaActual) { this.paginaActual = paginaActual; }

    public int getElementosPorPagina() { return elementosPorPagina; }
    public void setElementosPorPagina(int elementosPorPagina) { this.elementosPorPagina = elementosPorPagina; }

    public int getTotalElementos() { return totalElementos; }
    public void setTotalElementos(int totalElementos) { this.totalElementos = totalElementos; }

    @Override
    public String toString() {
        return new StringBuilder("Paginacion{")
                .append("pagina=").append(paginaActual)
                .append("/").append(getTotalPaginas())
                .append(", total=").append(totalElementos)
                .append("}").toString();
    }
}