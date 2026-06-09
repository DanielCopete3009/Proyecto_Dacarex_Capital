package com.dacarex.capital.servicio;

import com.dacarex.capital.dao.MovimientoDAO;
import com.dacarex.capital.enums.TipoMovimiento;
import com.dacarex.capital.modelo.Movimiento;

import java.time.LocalDate;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class MovimientoServicio {

    private final MovimientoDAO dao = new MovimientoDAO();

    // ─────────────────────────────────────
    // CRUD
    // ─────────────────────────────────────

    public void guardar(Movimiento m) {
        if (m.getDescripcion() == null || m.getDescripcion().isBlank())
            throw new IllegalArgumentException("La descripción es obligatoria.");
        if (m.getImporte() <= 0)
            throw new IllegalArgumentException("El importe debe ser mayor que 0.");
        if (m.getCategoria() == null)
            throw new IllegalArgumentException("La categoría es obligatoria.");
        if (m.getFecha() == null)
            throw new IllegalArgumentException("La fecha es obligatoria.");
        dao.guardar(m);
    }

    public void actualizar(Movimiento m) {
        if (m.getDescripcion() == null || m.getDescripcion().isBlank())
            throw new IllegalArgumentException("La descripción es obligatoria.");
        if (m.getImporte() <= 0)
            throw new IllegalArgumentException("El importe debe ser mayor que 0.");
        dao.actualizar(m);
    }

    public void eliminar(int id) {
        dao.eliminar(id);
    }

    public List<Movimiento> obtenerTodos() {
        return dao.buscarTodos();
    }

    // ─────────────────────────────────────
    // FILTROS CON STREAMS Y LAMBDAS
    // ─────────────────────────────────────

    public List<Movimiento> filtrarPorTipo(TipoMovimiento tipo) {
        return dao.buscarTodos().stream()
                .filter(m -> m.getTipo() == tipo)
                .collect(Collectors.toList());
    }

    public List<Movimiento> filtrarPorTexto(String texto) {
        if (texto == null || texto.isBlank()) return obtenerTodos();
        String lower = texto.toLowerCase();
        return dao.buscarTodos().stream()
                .filter(m -> m.getDescripcion().toLowerCase().contains(lower)
                          || (m.getNotas() != null && m.getNotas().toLowerCase().contains(lower)))
                .collect(Collectors.toList());
    }

    public List<Movimiento> filtrarPorFechas(LocalDate desde, LocalDate hasta) {
        return dao.buscarTodos().stream()
                .filter(m -> !m.getFecha().isBefore(desde))
                .filter(m -> !m.getFecha().isAfter(hasta))
                .sorted(Comparator.comparing(Movimiento::getFecha).reversed())
                .collect(Collectors.toList());
    }

    // ─────────────────────────────────────
    // CÁLCULOS PARA EL DASHBOARD
    // ─────────────────────────────────────

    public double calcularTotalIngresos() {
        return dao.buscarTodos().stream()
                .filter(m -> m.getTipo() == TipoMovimiento.INGRESO)
                .mapToDouble(Movimiento::getImporte)
                .sum();
    }

    public double calcularTotalGastos() {
        return dao.buscarTodos().stream()
                .filter(m -> m.getTipo() == TipoMovimiento.GASTO)
                .mapToDouble(Movimiento::getImporte)
                .sum();
    }

    public double calcularSaldo() {
        return calcularTotalIngresos() - calcularTotalGastos();
    }

    public String obtenerCategoriaMasActiva() {
        return dao.buscarTodos().stream()
                .collect(Collectors.groupingBy(
                    m -> m.getCategoria().getNombre(),
                    Collectors.counting()
                ))
                .entrySet().stream()
                .max(Map.Entry.comparingByValue())
                .map(Map.Entry::getKey)
                .orElse("Sin datos");
    }

    public Movimiento obtenerMayorIngreso() {
        return dao.buscarTodos().stream()
                .filter(m -> m.getTipo() == TipoMovimiento.INGRESO)
                .max(Comparator.comparingDouble(Movimiento::getImporte))
                .orElse(null);
    }

    public Movimiento obtenerMayorGasto() {
        return dao.buscarTodos().stream()
                .filter(m -> m.getTipo() == TipoMovimiento.GASTO)
                .max(Comparator.comparingDouble(Movimiento::getImporte))
                .orElse(null);
    }

    // Agrupa movimientos por categoría con su total
    public Map<String, Double> totalPorCategoria(TipoMovimiento tipo) {
        return dao.buscarTodos().stream()
                .filter(m -> m.getTipo() == tipo)
                .collect(Collectors.groupingBy(
                    m -> m.getCategoria().getNombre(),
                    Collectors.summingDouble(Movimiento::getImporte)
                ));
    }
}