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

        try (PreparedStatement ps = getConexion().prepareStatement(
                sql, Statement.RETURN_GENERATED_KEYS)) {

            ps.setString(1, c.getNombre());
            ps.setString(2, c.getTipo().name());
            ps.executeUpdate();

            ResultSet rs = ps.getGeneratedKeys();
            if (rs.next()) {
                c.setId(rs.getInt(1));
            }

        } catch (SQLException e) {
            mostrarError("guardar", e);
        }
    }

    @Override
    public Optional<Categoria> buscarPorId(int id) {
        String sql = "SELECT * FROM categorias WHERE id = ?";

        try (PreparedStatement ps = getConexion().prepareStatement(sql)) {
            ps.setInt(1, id);

            ResultSet rs = ps.executeQuery();
            return rs.next() ? Optional.of(mapear(rs)) : Optional.empty();

        } catch (SQLException e) {
            mostrarError("buscar", e);
            return Optional.empty();
        }
    }

    @Override
    public List<Categoria> buscarTodos() {
        return ejecutarConsulta(
                "SELECT * FROM categorias ORDER BY nombre",
                null
        );
    }

    public List<Categoria> buscarPorTipo(TipoMovimiento tipo) {
        return ejecutarConsulta(
                "SELECT * FROM categorias WHERE tipo = ? ORDER BY nombre",
                tipo.name()
        );
    }

    @Override
    public void actualizar(Categoria c) {
        String sql = "UPDATE categorias SET nombre = ?, tipo = ? WHERE id = ?";

        try (PreparedStatement ps = getConexion().prepareStatement(sql)) {
            ps.setString(1, c.getNombre());
            ps.setString(2, c.getTipo().name());
            ps.setInt(3, c.getId());
            ps.executeUpdate();

        } catch (SQLException e) {
            mostrarError("actualizar", e);
        }
    }

    @Override
    public void eliminar(int id) {
        String sql = "DELETE FROM categorias WHERE id = ?";

        try (PreparedStatement ps = getConexion().prepareStatement(sql)) {
            ps.setInt(1, id);
            ps.executeUpdate();

        } catch (SQLException e) {
            mostrarError("eliminar", e);
        }
    }

    private List<Categoria> ejecutarConsulta(String sql, String parametro) {
        List<Categoria> lista = new ArrayList<>();

        try (PreparedStatement ps = getConexion().prepareStatement(sql)) {

            if (parametro != null) {
                ps.setString(1, parametro);
            }

            ResultSet rs = ps.executeQuery();

            while (rs.next()) {
                lista.add(mapear(rs));
            }

        } catch (SQLException e) {
            mostrarError("consultar", e);
        }

        return lista;
    }

    private Categoria mapear(ResultSet rs) throws SQLException {
        return new Categoria(
                rs.getInt("id"),
                rs.getString("nombre"),
                TipoMovimiento.valueOf(rs.getString("tipo"))
        );
    }

    private void mostrarError(String accion, SQLException e) {
        System.out.println("Error al " + accion + " categoría: " + e.getMessage());
    }
}