package com.dacarex.capital.informe;

import com.dacarex.capital.dashboard.PuntoGrafico;

import java.util.List;

public class ResultadoInforme {

    private ParametrosInforme parametros;
    private List<PuntoGrafico> serieGrafico;
    private List<DistribucionGasto> distribucionGastos;
    private List<FilaResumenInforme> tablaResumen;
    private KPIsInforme kpis;

    public ResultadoInforme(ParametrosInforme parametros,
                            List<PuntoGrafico> serieGrafico,
                            List<DistribucionGasto> distribucionGastos,
                            List<FilaResumenInforme> tablaResumen,
                            KPIsInforme kpis) {
        this.parametros = parametros;
        this.serieGrafico = serieGrafico;
        this.distribucionGastos = distribucionGastos;
        this.tablaResumen = tablaResumen;
        this.kpis = kpis;
    }

    // Getters
    public ParametrosInforme getParametros() { return parametros; }
    public List<PuntoGrafico> getSerieGrafico() { return serieGrafico; }
    public List<DistribucionGasto> getDistribucionGastos() { return distribucionGastos; }
    public List<FilaResumenInforme> getTablaResumen() { return tablaResumen; }
    public KPIsInforme getKpis() { return kpis; }

    @Override
    public String toString() {
        return new StringBuilder("ResultadoInforme{")
                .append("parametros=").append(parametros)
                .append(", puntos=").append(serieGrafico != null ? serieGrafico.size() : 0)
                .append(", kpis=").append(kpis)
                .append("}").toString();
    }
}