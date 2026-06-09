package com.dacarex.capital.ui;

public class ModalConfirmacion {

    private boolean visible;
    private String titulo;
    private String descripcion;
    private Runnable onConfirmar;
    private Runnable onCancelar;

    public ModalConfirmacion(String titulo, String descripcion,
                             Runnable onConfirmar, Runnable onCancelar) {
        this.visible = true;
        this.titulo = titulo;
        this.descripcion = descripcion;
        this.onConfirmar = onConfirmar;
        this.onCancelar = onCancelar;
    }

    public void confirmar() {
        if (onConfirmar != null) onConfirmar.run();
        this.visible = false;
    }

    public void cancelar() {
        if (onCancelar != null) onCancelar.run();
        this.visible = false;
    }

    // Getters
    public boolean isVisible() { return visible; }
    public String getTitulo() { return titulo; }
    public String getDescripcion() { return descripcion; }
}