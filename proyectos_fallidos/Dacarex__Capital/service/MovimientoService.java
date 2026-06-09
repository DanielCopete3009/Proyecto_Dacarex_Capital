package com.dacarex.capital.service;

import com.dacarex.capital.dashboard.PuntoGrafico;
import com.dacarex.capital.dashboard.ResumenFinanciero;
import com.dacarex.capital.enums.PeriodoDashboard;
import com.dacarex.capital.enums.TipoMovimiento;
import com.dacarex.capital.exception.EntidadNoEncontradaException;
import com.dacarex.capital.exception.ValidacionException;
import com.dacarex.capital.informe.*;
import com.dacarex.capital.model.*;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.stream.Collectors;

public class MovimientoService {

    // TreeMap ordena automáticamente por clave (fecha+id)
    private final List<Movimiento> movimientos = new ArrayList<>();
    private final CategoriaService categoriaService;
    private double saldoActual = 0.0;

    public MovimientoService(CategoriaService categoriaService) {
        this.categoriaService = categoriaService;
    }

    public Movimiento crear(FormularioMovimiento form) {
        if (!form.esValido())
            throw new ValidacionException(form.validar());

        Categoria categoria = categoriaService.buscarPorId(form.getCategoriaId());

        if (form.getTipo() != categoria.getTipo())
            throw new ValidacionException(
                List.of("El tipo del movimiento no coincide con el tipo de la categoría.")
            );

        saldoActual += form.getTipo() == TipoMovimiento.INGRESO
                ? form.getImporte()
                : -form.getImporte();

        String id = "mov-" + UUID.randomUUID().toString().substring(0, 8);
        Movimiento nuevo = new Movimiento(
            id, form.getTipo(), form.getDescripcion(),
            form.getImporte(), categoria, form.getFecha(),
            saldoActual, form.getNotas()
        );

        movimientos.add(nuevo);
        categoriaService.marcarEnUso(categoria.getId());
        return nuevo;
    }

    public Movimiento buscarPorId(String id) {
        return movimientos.stream()
                .filter(m -> m.getId().equals(id))
                .findFirst()
                .orElseThrow(() -> new EntidadNoEncontradaException("Movimiento", id));
    }

    public List<Movimiento> buscarTodos() {
        return movimientos.stream()
                .sorted(Comparator.comparing(Movimiento::getFecha).reversed())
                .collect(Collectors.toList());
    }

    public List<Movimiento> filtrar(FiltrosMovimientos filtros) {
        return movimientos.stream()
                .filter(m -> filtrarPorTexto(m, filtros.getTextoBusqueda()))
                .filter(m -> filtrarPorFechaDesde(m, filtros.getFechaDesde()))
                .filter(m -> filtrarPorFechaHasta(m, filtros.getFechaHasta()))
                .filter(m -> filtrarPorCategoria(m, filtros.getCategoriaId()))
                .filter(m -> filtrarPorTipo(m, filtros.getTipo()))
                .sorted(Comparator.comparing(Movimiento::getFecha).reversed())
                .collect(Collectors.toList());
    }

    private boolean filtrarPorTexto(Movimiento m, String texto) {
        if (texto == null || texto.isBlank()) return true;
        String lower = texto.toLowerCase();
        return m.getDescripcion().toLowerCase().contains(lower)
            || (m.getNotas() != null && m.getNotas().toLowerCase().contains(lower));
    }

    private boolean filtrarPorFechaDesde(Movimiento m, LocalDate desde) {
        return desde == null || !m.getFecha().isBefore(desde);
    }

    private boolean filtrarPorFechaHasta(Movimiento m, LocalDate hasta) {
        return hasta == null || !m.getFecha().isAfter(hasta);
    }

    private boolean filtrarPorCategoria(Movimiento m, String categoriaId) {
        return categoriaId == null || m.getCategoria().getId().equals(categoriaId);
    }

    private boolean filtrarPorTipo(Movimiento m, TipoMovimiento tipo) {
        return tipo == null || m.getTipo() == tipo;
    }

    public void actualizar(String id, FormularioMovimiento form) {
        if (!form.esValido())
            throw new ValidacionException(form.validar());

        Movimiento existente = buscarPorId(id);
        Categoria categoria = categoriaService.buscarPorId(form.getCategoriaId());

        existente.setTipo(form.getTipo());
        existente.setDescripcion(form.getDescripcion());
        existente.setImporte(form.getImporte());
        existente.setCategoria(categoria);
        existente.setFecha(form.getFecha());
        existente.setNotas(form.getNotas());
        existente.marcarActualizado();
    }

    public void eliminar(String id) {
        Movimiento m = buscarPorId(id);
        movimientos.remove(m);
    }

    public ResumenFinanciero calcularResumen(PeriodoDashboard periodo) {
        List<Movimiento> filtrados = filtrarPorPeriodo(periodo);

        double totalIngresos = filtrados.stream()
                .filter(m -> m.getTipo() == TipoMovimiento.INGRESO)
                .mapToDouble(Movimiento::getImporte)
                .sum();

        double totalGastos = filtrados.stream()
                .filter(m -> m.getTipo() == TipoMovimiento.GASTO)
                .mapToDouble(Movimiento::getImporte)
                .sum();

        return new ResumenFinanciero(saldoActual, totalIngresos, totalGastos, periodo);
    }

    public ResultadoInforme generarInforme(ParametrosInforme params) {
        List<Movimiento> filtrados = filtrarPorParametros(params);

        // Serie gráfico agrupada por mes
        Map<String, List<Movimiento>> porMes = filtrados.stream()
                .collect(Collectors.groupingBy(m ->
                    m.getFecha().format(DateTimeFormatter.ofPattern("MMM yyyy"))
                ));

        List<PuntoGrafico> serie = porMes.entrySet().stream()
                .map(e -> {
                    double ing = e.getValue().stream()
                            .filter(m -> m.getTipo() == TipoMovimiento.INGRESO)
                            .mapToDouble(Movimiento::getImporte).sum();
                    double gas = e.getValue().stream()
                            .filter(m -> m.getTipo() == TipoMovimiento.GASTO)
                            .mapToDouble(Movimiento::getImporte).sum();
                    return new PuntoGrafico(e.getKey(), ing, gas);
                })
                .collect(Collectors.toList());

        // Distribución de gastos
        double totalGastos = filtrados.stream()
                .filter(m -> m.getTipo() == TipoMovimiento.GASTO)
                .mapToDouble(Movimiento::getImporte).sum();

        List<DistribucionGasto> distribucion = filtrados.stream()
                .filter(m -> m.getTipo() == TipoMovimiento.GASTO)
                .collect(Collectors.groupingBy(
                    m -> m.getCategoria().getNombre(),
                    Collectors.summingDouble(Movimiento::getImporte)
                ))
                .entrySet().stream()
                .map(e -> new DistribucionGasto(e.getKey(), e.getValue(), totalGastos))
                .sorted(Comparator.comparingDouble(DistribucionGasto::getImporte).reversed())
                .collect(Collectors.toList());

        // Tabla resumen por categoría
        List<FilaResumenInforme> tabla = filtrados.stream()
                .collect(Collectors.groupingBy(m -> m.getCategoria().getNombre()))
                .entrySet().stream()
                .map(e -> {
                    double ing = e.getValue().stream()
                            .filter(m -> m.getTipo() == TipoMovimiento.INGRESO)
                            .mapToDouble(Movimiento::getImporte).sum();
                    double gas = e.getValue().stream()
                            .filter(m -> m.getTipo() == TipoMovimiento.GASTO)
                            .mapToDouble(Movimiento::getImporte).sum();
                    return new FilaResumenInforme(e.getKey(), ing, gas);
                })
                .collect(Collectors.toList());

        // KPIs
        double mayorIngreso = filtrados.stream()
                .filter(m -> m.getTipo() == TipoMovimiento.INGRESO)
                .mapToDouble(Movimiento::getImporte)
                .max().orElse(0);

        double mayorGasto = filtrados.stream()
                .filter(m -> m.getTipo() == TipoMovimiento.GASTO)
                .mapToDouble(Movimiento::getImporte)
                .max().orElse(0);

        String catMasActiva = filtrados.stream()
                .collect(Collectors.groupingBy(
                    m -> m.getCategoria().getNombre(),
                    Collectors.counting()
                ))
                .entrySet().stream()
                .max(Map.Entry.comparingByValue())
                .map(Map.Entry::getKey)
                .orElse("N/A");

        KPIsInforme kpis = new KPIsInforme(saldoActual, mayorIngreso, mayorGasto, catMasActiva);

        return new ResultadoInforme(params, serie, distribucion, tabla, kpis);
    }

    private List<Movimiento> filtrarPorPeriodo(PeriodoDashboard periodo) {
        LocalDate hoy = LocalDate.now();
        LocalDate desde = switch (periodo) {
            case MES       -> hoy.withDayOfMonth(1);
            case TRIMESTRE -> hoy.withDayOfMonth(1).minusMonths(2);
            case ANIO      -> hoy.withDayOfYear(1);
        };
        return movimientos.stream()
                .filter(m -> !m.getFecha().isBefore(desde))
                .collect(Collectors.toList());
    }

    private List<Movimiento> filtrarPorParametros(ParametrosInforme params) {
        LocalDate hoy = LocalDate.now();
        LocalDate desde;
        LocalDate hasta = hoy;

        if (params.esPersonalizado()) {
            desde = params.getFechaDesde();
            hasta = params.getFechaHasta();
        } else {
            desde = switch (params.getPeriodo()) {
                case MENSUAL      -> hoy.withDayOfMonth(1);
                case TRIMESTRAL   -> hoy.withDayOfMonth(1).minusMonths(2);
                case ANUAL        -> hoy.withDayOfYear(1);
                case PERSONALIZADO -> hoy.withDayOfMonth(1);
            };
        }

        LocalDate fechaDesde = desde;
        LocalDate fechaHasta = hasta;

        return movimientos.stream()
                .filter(m -> !m.getFecha().isBefore(fechaDesde)
                          && !m.getFecha().isAfter(fechaHasta))
                .collect(Collectors.toList());
    }

    public double getSaldoActual() { return saldoActual; }

    public int contarMovimientos() { return movimientos.size(); }
}