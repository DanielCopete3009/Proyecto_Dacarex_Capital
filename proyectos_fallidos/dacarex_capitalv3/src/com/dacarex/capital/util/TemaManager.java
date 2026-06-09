package com.dacarex.capital.util;

import java.awt.Color;

public class TemaManager {

    private static boolean modoOscuro = false;

    public static void toggleTema() {
        modoOscuro = !modoOscuro;
    }

    public static boolean isModoOscuro() {
        return modoOscuro;
    }

    // ── Fondos ──
    public static Color getFondoPrincipal() {
        return modoOscuro ? new Color(18, 18, 30) : new Color(245, 247, 250);
    }

    public static Color getFondoPanel() {
        return modoOscuro ? new Color(28, 28, 45) : Color.WHITE;
    }

    public static Color getFondoSidebar() {
        return modoOscuro ? new Color(15, 15, 25) : new Color(30, 30, 45);
    }

    public static Color getFondoTabla() {
        return modoOscuro ? new Color(28, 28, 45) : Color.WHITE;
    }

    public static Color getFondoCabecera() {
        return modoOscuro ? new Color(15, 15, 25) : new Color(30, 30, 45);
    }

    // ── Textos ──
    public static Color getTextoTitulo() {
        return modoOscuro ? new Color(220, 220, 240) : new Color(30, 30, 45);
    }

    public static Color getTextoNormal() {
        return modoOscuro ? new Color(180, 180, 200) : new Color(60, 60, 80);
    }

    public static Color getTextoSubtitulo() {
        return modoOscuro ? new Color(130, 130, 160) : new Color(100, 100, 120);
    }

    // ── Bordes ──
    public static Color getBorde() {
        return modoOscuro ? new Color(50, 50, 75) : new Color(220, 220, 230);
    }

    // ── Acento ──
    public static Color getAcento() {
        return new Color(100, 180, 255);
    }

    public static String getIconoTema() {
        return modoOscuro ? "☀ Modo claro" : "🌙 Modo oscuro";
    }
}