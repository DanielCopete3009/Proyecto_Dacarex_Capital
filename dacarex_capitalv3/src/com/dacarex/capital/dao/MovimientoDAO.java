package com.dacarex.capital.dao;

import com.dacarex.capital.enums.TipoMovimiento;
import com.dacarex.capital.modelo.Categoria;
import com.dacarex.capital.modelo.Movimiento;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class MovimientoDAO extends DAOBase<Movimiento> {

    private final CategoriaDAO categoriaDAO = new CategoriaDAO();

    @Override
    public void guardar(Movimiento m) {
        String sql = """
            INSERT INTO movimientos
            (tipo, descripcion, importe, categoria_id, fecha, notas)
            VALUES (?, ?, ?, ?, ?, ?)
            """;
        try (PreparedStatement ps = getConexion().prepareStatement(sql,
                Statement.RETURN_GENERATED_KEYS)) {
            ps.setString(1, m.getTipo().name());
            ps.setString(2, m.getDescripcion());
            ps.setDouble(3, m.getImporte());
            ps.setInt(4, m.getCategoria().getId());
            ps.setDate(5, Date.valueOf(m.getFecha()));
            ps.setString(6, m.getNotas());
            ps.executeUpdate();
            ResultSet rs = ps.getGeneratedKeys();
            if (rs.next()) m.setId(rs.getInt(1));
            System.out.println("✔ Movimiento guardado: " + m.getDescripcion());
        } catch (SQLException e) {
            System.out.println("✘ Error al guardar movimiento: " + e.getMessage());
        }
    }

    @Override
    public Optional<Movimiento> buscarPorId(int id) {
        String sql = "SELECT * FROM movimientos WHERE id = ?";
        try (PreparedStatement ps = getConexion().prepareStatement(sql)) {
            ps.setInt(1, id);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) return Optional.of(mapear(rs));
        } catch (SQLException e) {
            System.out.println(" Error al buscar movimiento: " + e.getMessage());
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
            System.out.println(" Error al listar movimientos: " + e.getMessage());
        }
        return lista;
    }

    public List<Movimiento> buscarPorTipo(TipoMovimiento tipo) {
        List<Movimiento> lista = new ArrayList<>();
        String sql = "SELECT * FROM movimientos WHERE tipo = ? ORDER BY fecha DESC";
        try (PreparedStatement ps = getConexion().prepareStatement(sql)) {
            ps.setString(1, tipo.name());
            ResultSet rs = ps.executeQuery();
            while (rs.next()) lista.add(mapear(rs));
        } catch (SQLException e) {
            System.out.println(" Error al buscar por tipo: " + e.getMessage());
        }
        return lista;
    }

    @Override
    public void actualizar(Movimiento m) {
        String sql = """
            UPDATE movimientos SET
            tipo = ?, descripcion = ?, importe = ?,
            categoria_id = ?, fecha = ?, notas = ?
            WHERE id = ?
            """;
        try (PreparedStatement ps = getConexion().prepareStatement(sql)) {
            ps.setString(1, m.getTipo().name());
            ps.setString(2, m.getDescripcion());
            ps.setDouble(3, m.getImporte());
            ps.setInt(4, m.getCategoria().getId());
            ps.setDate(5, Date.valueOf(m.getFecha()));
            ps.setString(6, m.getNotas());
            ps.setInt(7, m.getId());
            ps.executeUpdate();
            System.out.println(" Movimiento actualizado: " + m.getDescripcion());
        } catch (SQLException e) {
            System.out.println(" Error al actualizar movimiento: " + e.getMessage());
        }
    }

    @Override
    public void eliminar(int id) {
        String sql = "DELETE FROM movimientos WHERE id = ?";
        try (PreparedStatement ps = getConexion().prepareStatement(sql)) {
            ps.setInt(1, id);
            ps.executeUpdate();
            System.out.println(" Movimiento eliminado: " + id);
        } catch (SQLException e) {
            System.out.println(" Error al eliminar movimiento: " + e.getMessage());
        }
    }

    private Movimiento mapear(ResultSet rs) throws SQLException {
        Categoria categoria = categoriaDAO
                .buscarPorId(rs.getInt("categoria_id"))
                .orElse(new Categoria(0, "Sin categoría", TipoMovimiento.GASTO));

        return new Movimiento(
            rs.getInt("id"),
            TipoMovimiento.valueOf(rs.getString("tipo")),
            rs.getString("descripcion"),
            rs.getDouble("importe"),
            categoria,
            rs.getDate("fecha").toLocalDate(),
            rs.getString("notas")
        );
    }
}