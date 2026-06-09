package com.dacarex.capital.dao;

import com.dacarex.capital.enums.TipoCuenta;
import com.dacarex.capital.modelo.Usuario;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class UsuarioDAO extends DAOBase<Usuario> {

    @Override
    public void guardar(Usuario u) {
        String sql = "INSERT INTO usuarios (nombre, email, contrasenia, tipo_cuenta) VALUES (?, ?, ?, ?)";
        try (PreparedStatement ps = getConexion().prepareStatement(sql,
                Statement.RETURN_GENERATED_KEYS)) {
            ps.setString(1, u.getNombre());
            ps.setString(2, u.getEmail());
            ps.setString(3, u.getContrasenia());
            ps.setString(4, u.getTipoCuenta().name());
            ps.executeUpdate();
            ResultSet rs = ps.getGeneratedKeys();
            if (rs.next()) u.setId(rs.getInt(1));
            System.out.println(" Usuario guardado: " + u.getEmail());
        } catch (SQLException e) {
            System.out.println(" Error al guardar usuario: " + e.getMessage());
        }
    }

    @Override
    public Optional<Usuario> buscarPorId(int id) {
        String sql = "SELECT * FROM usuarios WHERE id = ?";
        try (PreparedStatement ps = getConexion().prepareStatement(sql)) {
            ps.setInt(1, id);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) return Optional.of(mapear(rs));
        } catch (SQLException e) {
            System.out.println(" Error al buscar usuario: " + e.getMessage());
        }
        return Optional.empty();
    }

    public Optional<Usuario> buscarPorEmail(String email) {
        String sql = "SELECT * FROM usuarios WHERE email = ?";
        try (PreparedStatement ps = getConexion().prepareStatement(sql)) {
            ps.setString(1, email);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) return Optional.of(mapear(rs));
        } catch (SQLException e) {
            System.out.println(" Error al buscar por email: " + e.getMessage());
        }
        return Optional.empty();
    }

    @Override
    public List<Usuario> buscarTodos() {
        List<Usuario> lista = new ArrayList<>();
        String sql = "SELECT * FROM usuarios ORDER BY nombre";
        try (Statement st = getConexion().createStatement();
             ResultSet rs = st.executeQuery(sql)) {
            while (rs.next()) lista.add(mapear(rs));
        } catch (SQLException e) {
            System.out.println(" Error al listar usuarios: " + e.getMessage());
        }
        return lista;
    }

    @Override
    public void actualizar(Usuario u) {
        String sql = "UPDATE usuarios SET nombre = ?, email = ?, contrasenia = ?, tipo_cuenta = ? WHERE id = ?";
        try (PreparedStatement ps = getConexion().prepareStatement(sql)) {
            ps.setString(1, u.getNombre());
            ps.setString(2, u.getEmail());
            ps.setString(3, u.getContrasenia());
            ps.setString(4, u.getTipoCuenta().name());
            ps.setInt(5, u.getId());
            ps.executeUpdate();
            System.out.println(" Usuario actualizado: " + u.getEmail());
        } catch (SQLException e) {
            System.out.println(" Error al actualizar usuario: " + e.getMessage());
        }
    }

    @Override
    public void eliminar(int id) {
        String sql = "DELETE FROM usuarios WHERE id = ?";
        try (PreparedStatement ps = getConexion().prepareStatement(sql)) {
            ps.setInt(1, id);
            ps.executeUpdate();
            System.out.println(" Usuario eliminado: " + id);
        } catch (SQLException e) {
            System.out.println(" Error al eliminar usuario: " + e.getMessage());
        }
    }

    private Usuario mapear(ResultSet rs) throws SQLException {
        return new Usuario(
            rs.getInt("id"),
            rs.getString("nombre"),
            rs.getString("email"),
            rs.getString("contrasenia"),
            TipoCuenta.valueOf(rs.getString("tipo_cuenta"))
        );
    }
}