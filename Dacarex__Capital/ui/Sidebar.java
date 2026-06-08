package com.dacarex.capital.ui;

import java.util.Arrays;
import java.util.List;

public class Sidebar {

    private List<ItemNavegacion> items;
    private double saldoActual;
    private boolean collapsed;

    public Sidebar(List<ItemNavegacion> items, double saldoActual, boolean collapsed) {
        this.items = items;
        this.saldoActual = saldoActual;
        this.collapsed = collapsed;
    }

    public void setActivo(String ruta) {
        items.forEach(item -> item.setActivo(
            java.util.Objects.equals(item.getRuta(), ruta)
        ));
    }

    public static Sidebar porDefecto(double saldoActual) {
        return new Sidebar(Arrays.asList(
            new ItemNavegacion("Dashboard",        "/dashboard",         "🏠"),
            new ItemNavegacion("Movimientos",      "/movimientos",       "💸"),
            new ItemNavegacion("Nuevo movimiento", "/movimientos/nuevo", "➕"),
            new ItemNavegacion("Informes",         "/informes",          "📊"),
            new ItemNavegacion("Categorías",       "/categorias",        "🗂️"),
            new ItemNavegacion("Configuración",    "/configuracion",     "⚙️")
        ), saldoActual, false);
    }

    // Getters y Setters
    public List<ItemNavegacion> getItems() { return items; }
    public void setItems(List<ItemNavegacion> items) { this.items = items; }

    public double getSaldoActual() { return saldoActual; }
    public void setSaldoActual(double saldoActual) { this.saldoActual = saldoActual; }

    public boolean isCollapsed() { return collapsed; }
    public void setCollapsed(boolean collapsed) { this.collapsed = collapsed; }
}