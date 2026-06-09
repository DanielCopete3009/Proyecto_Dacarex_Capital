package com.dacarex.capital.dao;

import com.dacarex.capital.db.ConexionBD;
import com.dacarex.capital.enums.TipoMovimiento;
import com.dacarex.capital.exception.PersistenciaException;
import com.dacarex.capital.io.Logger;
import com.dacarex.capital.model.Categoria;
import com.dacarex.capital.model.FiltrosMovimientos;
import com.dacarex.capital.model.Movimiento;
import com.dacarex.capital.service.IRepositorio;

import java.sql.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class MovimientoDAO implements IRepositorio<Movimiento, String> {

    private final CategoriaDAO categoriaDAO;

    public MovimientoDAO(CategoriaDAO categoriaDAO) {
        this.categoriaDAO = categoriaDAO;
    }

    private Connection getConexion() {
        return ConexionBD.getInstance().getConexion();
    }

    @Override
    public void guardar(Movimiento m) {
        String sql = """
            INSERT INTO movimientos
            (id, tipo, descripcion, importe, categoria_id, fecha,
             notas, saldo_resultante, creado_en, actualizado_en)
            VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?)
            """;
        try (PreparedStatement ps = getConexion().prepareStatement(sql)) {
            ps.setString(1, m.getId());
            ps.setString(2, m.getTipo().name());
            ps.setString(3, m.getDescripcion());
            ps.setDouble(4, m.getImporte());
            ps.setString(5, m.getCategoria().getId());
            ps.setDate(6, Date.valueOf(m.getFecha()));
            ps.setString(7, m.getNotas());
            ps.setDouble(8, m.getSaldoResultante());
            ps.setTimestamp(9, Timestamp.valueOf(m.getCreadoEn()));
            ps.setTimestamp(10, Timestamp.valueOf(m.getActualizadoEn()));
            ps.executeUpdate();
            Logger.info("Movimiento guardado: " + m.getDescripcion());
        } catch (SQLException e) {
            Logger.error("Error al guardar movimiento: " + e.getMessage());
            throw new PersistenciaException("No se pudo guardar el movimiento.", e);
        }
    }

    @Override
    public Optional<Movimiento> buscarPorId(String id) {
        String sql = "SELECT * FROM movimientos WHERE id = ?";
        try (PreparedStatement ps = getConexion().prepareStatement(sql)) {
            ps.setString(1, id);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) return Optional.of(mapear(rs));
        } catch (SQLException e) {
            Logger.error("Error al buscar movimiento: " + e.getMessage());
            throw new PersistenciaException("Error al buscar movimiento.", e);
        }
        return Optional.empty();
    }

    @Override
    public List<Movimiento> buscarTodos() {
        List<Movimiento> lista = new ArrayList<>();
        String sql = "SELECT * FROM movimientos ORDER BY fecha DESC";
        try (Statement st = getConexion().createStatement();
             ResultSet rs = st.executeQuery(sql)) {
            while (rs.next()) lista.add(mapear(rs));
        } catch (SQLException e) {
            Logger.error("Error al listar movimientos: " + e.getMessage());
            throw new PersistenciaException("Error al listar movimientos.", e);
        }
        return lista;
    }

    public List<Movimiento> buscarConFiltros(FiltrosMovimientos f) {
        List<Movimiento> lista = new ArrayList<>();
        StringBuilder sql = new StringBuilder(
            "SELECT * FROM movimientos WHERE 1=1"
        );

        if (f.getTipo() != null)
            sql.append(" AND tipo = '").append(f.getTipo().name()).append("'");
        if (f.getCategoriaId() != null)
            sql.append(" AND categoria_id = '").append(f.getCategoriaId()).append("'");
        if (f.getFechaDesde() != null)
            sql.append(" AND fecha >= '").append(f.getFechaDesde()).append("'");
        if (f.getFechaHasta() != null)
            sql.append(" AND fecha <= '").append(f.getFechaHasta()).append("'");
        if (f.getTextoBusqueda() != null && !f.getTextoBusqueda().isBlank())
            sql.append(" AND descripcion LIKE '%").append(f.getTextoBusqueda()).append("%'");

        sql.append(" ORDER BY fecha DESC");

        try (Statement st = getConexion().createStatement();
             ResultSet rs = st.executeQuery(sql.toString())) {
            while (rs.next()) lista.add(mapear(rs));
        } catch (SQLException e) {
            Logger.error("Error al filtrar movimientos: " + e.getMessage());
            throw new PersistenciaException("Error al filtrar movimientos.", e);
        }
        return lista;
    }

    @Override
    public void actualizar(Movimiento m) {
        String sql = """
            UPDATE movimientos SET
            tipo = ?, descripcion = ?, importe = ?,
            categoria_id = ?, fecha = ?, notas = ?,
            saldo_resultante = ?, actualizado_en = ?
            WHERE id = ?
            """;
        try (PreparedStatement ps = getConexion().prepareStatement(sql)) {
            ps.setString(1, m.getTipo().name());
            ps.setString(2, m.getDescripcion());
            ps.setDouble(3, m.getImporte());
            ps.setString(4, m.getCategoria().getId());
            ps.setDate(5, Date.valueOf(m.getFecha()));
            ps.setString(6, m.getNotas());
            ps.setDouble(7, m.getSaldoResultante());
            ps.setTimestamp(8, Timestamp.valueOf(LocalDateTime.now()));
            ps.setString(9, m.getId());
            ps.executeUpdate();
            Logger.info("Movimiento actualizado: " + m.getId());
        } catch (SQLException e) {
            Logger.error("Error al actualizar movimiento: " + e.getMessage());
            throw new PersistenciaException("No se pudo actualizar el movimiento.", e);
        }
    }

    @Override
    public void eliminar(String id) {
        String sql = "DELETE FROM movimientos WHERE id = ?";
        try (PreparedStatement ps = getConexion().prepareStatement(sql)) {
            ps.setString(1, id);
            ps.executeUpdate();
            Logger.info("Movimiento eliminado: " + id);
        } catch (SQLException e) {
            Logger.error("Error al eliminar movimiento: " + e.getMessage());
            throw new PersistenciaException("No se pudo eliminar el movimiento.", e);
        }
    }

    private Movimiento mapear(ResultSet rs) throws SQLException {
        String categoriaId = rs.getString("categoria_id");
        Categoria categoria = categoriaDAO.buscarPorId(categoriaId)
                .orElse(new Categoria(categoriaId, "Desconocida",
                        TipoMovimiento.GASTO));

        return new Movimiento(
            rs.getString("id"),
            TipoMovimiento.valueOf(rs.getString("tipo")),
            rs.getString("descripcion"),
            rs.getDouble("importe"),
            categoria,
            rs.getDate("fecha").toLocalDate(),
            rs.getDouble("saldo_resultante"),
            rs.getString("notas")
        );
    }
}