package com.dacarex.capital.ui;

import com.dacarex.capital.enums.FormatoExportacion;
import com.dacarex.capital.informe.ParametrosInforme;
import com.dacarex.capital.model.FiltrosMovimientos;

public class SolicitudExportacion {

    private FormatoExportacion formato;
    private FiltrosMovimientos filtros;
    private ParametrosInforme parametrosInforme;

    public SolicitudExportacion(FormatoExportacion formato) {
        this.formato = formato;
    }

    public SolicitudExportacion(FormatoExportacion formato, FiltrosMovimientos filtros,
                                ParametrosInforme parametrosInforme) {
        this.formato = formato;
        this.filtros = filtros;
        this.parametrosInforme = parametrosInforme;
    }

    // Getters y Setters
    public FormatoExportacion getFormato() { return formato; }
    public void setFormato(FormatoExportacion formato) { this.formato = formato; }

    public FiltrosMovimientos getFiltros() { return filtros; }
    public void setFiltros(FiltrosMovimientos filtros) { this.filtros = filtros; }

    public ParametrosInforme getParametrosInforme() { return parametrosInforme; }
    public void setParametrosInforme(ParametrosInforme p) { this.parametrosInforme = p; }
}