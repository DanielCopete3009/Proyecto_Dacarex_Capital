package com.dacarex.capital.ui;

public class ItemNavegacion {

    private String etiqueta;
    private String ruta;
    private String icono;
    private boolean activo;

    public ItemNavegacion(String etiqueta, String ruta, String icono) {
        this(etiqueta, ruta, icono, false);
    }

    public ItemNavegacion(String etiqueta, String ruta, String icono, boolean activo) {
        this.etiqueta = etiqueta;
        this.ruta = ruta;
        this.icono = icono;
        this.activo = activo;
    }

    // Getters y Setters
    public String getEtiqueta() { return etiqueta; }
    public void setEtiqueta(String etiqueta) { this.etiqueta = etiqueta; }

    public String getRuta() { return ruta; }
    public void setRuta(String ruta) { this.ruta = ruta; }

    public String getIcono() { return icono; }
    public void setIcono(String icono) { this.icono = icono; }

    public boolean isActivo() { return activo; }
    public void setActivo(boolean activo) { this.activo = activo; }

    @Override
    public String toString() {
        return new StringBuilder("ItemNavegacion{")
                .append("etiqueta=").append(etiqueta)
                .append(", ruta=").append(ruta)
                .append(", activo=").append(activo)
                .append("}").toString();
    }
}