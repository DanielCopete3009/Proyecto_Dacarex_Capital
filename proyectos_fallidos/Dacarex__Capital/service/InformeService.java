package com.dacarex.capital.service;

import com.dacarex.capital.enums.FormatoExportacion;
import com.dacarex.capital.informe.ParametrosInforme;
import com.dacarex.capital.informe.ResultadoInforme;
import com.dacarex.capital.model.FiltrosMovimientos;
import com.dacarex.capital.model.Movimiento;
import com.dacarex.capital.ui.SolicitudExportacion;

import java.util.List;

public class InformeService {

    private final MovimientoService movimientoService;

    public InformeService(MovimientoService movimientoService) {
        this.movimientoService = movimientoService;
    }

    public ResultadoInforme generar(ParametrosInforme params) {
        return movimientoService.generarInforme(params);
    }

    public String exportar(SolicitudExportacion solicitud) {
        List<Movimiento> datos = solicitud.getFiltros() != null
                ? movimientoService.filtrar(solicitud.getFiltros())
                : movimientoService.buscarTodos();

        FormatoExportacion formato = solicitud.getFormato();

        return switch (formato) {
            case CSV   -> generarCsv(datos);
            case PDF   -> "exportacion.pdf (pendiente librería iText)";
            case EXCEL -> "exportacion.xlsx (pendiente librería Apache POI)";
        };
    }

    private String generarCsv(List<Movimiento> movimientos) {
        StringBuilder sb = new StringBuilder();
        sb.append("Fecha,Tipo,Descripcion,Importe,Categoria,Notas\n");
        movimientos.forEach(m -> sb.append(m.toCsv()).append("\n"));
        return sb.toString();
    }
}