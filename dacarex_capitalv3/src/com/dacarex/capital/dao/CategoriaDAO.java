package com.dacarex.capital.dao;

import com.dacarex.capital.enums.TipoMovimiento;
import com.dacarex.capital.modelo.Categoria;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class CategoriaDAO extends DAOBase<Categoria> {

    @Override
    public void guardar(Categoria c) {
        String sql = "INSERT INTO categorias (nombre, tipo) VALUES (?, ?)";
        try (PreparedStatement ps = getConexion().prepareStatement(sql,
                Statement.RETURN_GENERATED_KEYS)) {
            ps.setString(1, c.getNombre());
            ps.setString(2, c.getTipo().name());
            ps.executeUpdate();
            ResultSet rs = ps.getGeneratedKeys();
            if (rs.next()) c.setId(rs.getInt(1));
            System.out.println("✔ Categoría guardada: " + c.getNombre());
        } catch (SQLException e) {
            System.out.println("✘ Error al guardar categoría: " + e.getMessage());
        }
    }

    @Override
    public Optional<Categoria> buscarPorId(int id) {
        String sql = "SELECT * FROM categorias WHERE id = ?";
        try (PreparedStatement ps = getConexion().prepareStatement(sql)) {
            ps.setInt(1, id);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) return Optional.of(mapear(rs));
        } catch (SQLException e) {
            System.out.println("✘ Error al buscar categoría: " + e.getMessage());
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
            System.out.println("✘ Error al listar categorías: " + e.getMessage());
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
            System.out.println("✘ Error al buscar por tipo: " + e.getMessage());
        }
        return lista;
    }

    @Override
    public void actualizar(Categoria c) {
        String sql = "UPDATE categorias SET nombre = ?, tipo = ? WHERE id = ?";
        try (PreparedStatement ps = getConexion().prepareStatement(sql)) {
            ps.setString(1, c.getNombre());
            ps.setString(2, c.getTipo().name());
            ps.setInt(3, c.getId());
            ps.executeUpdate();
            System.out.println("✔ Categoría actualizada: " + c.getNombre());
        } catch (SQLException e) {
            System.out.println("✘ Error al actualizar categoría: " + e.getMessage());
        }
    }

    @Override
    public void eliminar(int id) {
        String sql = "DELETE FROM categorias WHERE id = ?";
        try (PreparedStatement ps = getConexion().prepareStatement(sql)) {
            ps.setInt(1, id);
            ps.executeUpdate();
            System.out.println("✔ Categoría eliminada: " + id);
        } catch (SQLException e) {
            System.out.println("✘ Error al eliminar categoría: " + e.getMessage());
        }
    }

    private Categoria mapear(ResultSet rs) throws SQLException {
        return new Categoria(
            rs.getInt("id"),
            rs.getString("nombre"),
            TipoMovimiento.valueOf(rs.getString("tipo"))
        );
    }
}