package com.dacarex.capital.model;

import com.dacarex.capital.enums.FormatoFecha;
import com.dacarex.capital.enums.Tema;

public class PreferenciasUsuario {

    private String monedaPredeterminada;
    private FormatoFecha formatoFecha;
    private Tema tema;

    public PreferenciasUsuario() {
        this.monedaPredeterminada = "EUR";
        this.formatoFecha = FormatoFecha.DD_MM_YYYY;
        this.tema = Tema.CLARO;
    }

    public PreferenciasUsuario(String monedaPredeterminada, FormatoFecha formatoFecha, Tema tema) {
        this.monedaPredeterminada = monedaPredeterminada;
        this.formatoFecha = formatoFecha;
        this.tema = tema;
    }

    // Getters y Setters
    public String getMonedaPredeterminada() { return monedaPredeterminada; }
    public void setMonedaPredeterminada(String m) { this.monedaPredeterminada = m; }

    public FormatoFecha getFormatoFecha() { return formatoFecha; }
    public void setFormatoFecha(FormatoFecha formatoFecha) { this.formatoFecha = formatoFecha; }

    public Tema getTema() { return tema; }
    public void setTema(Tema tema) { this.tema = tema; }

    @Override
    public String toString() {
        return new StringBuilder("PreferenciasUsuario{")
                .append("moneda=").append(monedaPredeterminada)
                .append(", fecha=").append(formatoFecha)
                .append(", tema=").append(tema)
                .append("}").toString();
    }
}