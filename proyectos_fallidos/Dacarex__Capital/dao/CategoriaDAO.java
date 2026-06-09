package com.dacarex.capital.dao;

import com.dacarex.capital.db.ConexionBD;
import com.dacarex.capital.enums.TipoMovimiento;
import com.dacarex.capital.exception.PersistenciaException;
import com.dacarex.capital.io.Logger;
import com.dacarex.capital.model.Categoria;
import com.dacarex.capital.service.IRepositorio;

import java.sql.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class CategoriaDAO implements IRepositorio<Categoria, String> {

    private Connection getConexion() {
        return ConexionBD.getInstance().getConexion();
    }

    @Override
    public void guardar(Categoria c) {
        String sql = """
            INSERT INTO categorias
            (id, nombre, tipo, en_uso, creado_en, actualizado_en)
            VALUES (?, ?, ?, ?, ?, ?)
            """;
        try (PreparedStatement ps = getConexion().prepareStatement(sql)) {
            ps.setString(1, c.getId());
            ps.setString(2, c.getNombre());
            ps.setString(3, c.getTipo().name());
            ps.setBoolean(4, c.isEnUso());
            ps.setTimestamp(5, Timestamp.valueOf(c.getCreadoEn()));
            ps.setTimestamp(6, Timestamp.valueOf(c.getActualizadoEn()));
            ps.executeUpdate();
            Logger.info("Categoría guardada: " + c.getNombre());
        } catch (SQLException e) {
            Logger.error("Error al guardar categoría: " + e.getMessage());
            throw new PersistenciaException("No se pudo guardar la categoría.", e);
        }
    }

    @Override
    public Optional<Categoria> buscarPorId(String id) {
        String sql = "SELECT * FROM categorias WHERE id = ?";
        try (PreparedStatement ps = getConexion().prepareStatement(sql)) {
            ps.setString(1, id);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) return Optional.of(mapear(rs));
        } catch (SQLException e) {
            Logger.error("Error al buscar categoría: " + e.getMessage());
            throw new PersistenciaException("Error al buscar categoría.", e);
        }
        return Optional.empty();
    }

    @Override
    public List<Categoria> buscarTodos() {
        List<Categoria> lista = new ArrayList<>();
        String sql = "SELECT * FROM categorias ORDER BY nombre";
        try (Statement st = getConexion().createStatement();
             ResultSet rs = st.executeQuery(sql)) {
            while (rs.next()) lista.add(mapear(rs));
        } catch (SQLException e) {
            Logger.error("Error al listar categorías: " + e.getMessage());
            throw new PersistenciaException("Error al listar categorías.", e);
        }
        return lista;
    }

    public List<Categoria> buscarPorTipo(TipoMovimiento tipo) {
        List<Categoria> lista = new ArrayList<>();
        String sql = "SELECT * FROM categorias WHERE tipo = ? ORDER BY nombre";
        try (PreparedStatement ps = getConexion().prepareStatement(sql)) {
            ps.setString(1, tipo.name());
            ResultSet rs = ps.executeQuery();
            while (rs.next()) lista.add(mapear(rs));
        } catch (SQLException e) {
            Logger.error("Error al buscar categorías por tipo: " + e.getMessage());
            throw new PersistenciaException("Error al buscar categorías.", e);
        }
        return lista;
    }

    @Override
    public void actualizar(Categoria c) {
        String sql = """
            UPDATE categorias SET
            nombre = ?, tipo = ?, en_uso = ?, actualizado_en = ?
            WHERE id = ?
            """;
        try (PreparedStatement ps = getConexion().prepareStatement(sql)) {
            ps.setString(1, c.getNombre());
            ps.setString(2, c.getTipo().name());
            ps.setBoolean(3, c.isEnUso());
            ps.setTimestamp(4, Timestamp.valueOf(LocalDateTime.now()));
            ps.setString(5, c.getId());
            ps.executeUpdate();
            Logger.info("Categoría actualizada: " + c.getNombre());
        } catch (SQLException e) {
            Logger.error("Error al actualizar categoría: " + e.getMessage());
            throw new PersistenciaException("No se pudo actualizar la categoría.", e);
        }
    }

    @Override
    public void eliminar(String id) {
        String sql = "DELETE FROM categorias WHERE id = ?";
        try (PreparedStatement ps = getConexion().prepareStatement(sql)) {
            ps.setString(1, id);
            ps.executeUpdate();
            Logger.info("Categoría eliminada: " + id);
        } catch (SQLException e) {
            Logger.error("Error al eliminar categoría: " + e.getMessage());
            throw new PersistenciaException("No se pudo eliminar la categoría.", e);
        }
    }

    private Categoria mapear(ResultSet rs) throws SQLException {
        return new Categoria(
            rs.getString("id"),
            rs.getString("nombre"),
            TipoMovimiento.valueOf(rs.getString("tipo")),
            rs.getBoolean("en_uso")
        );
    }
}