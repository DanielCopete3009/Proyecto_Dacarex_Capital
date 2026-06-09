package com.dacarex.capital.dao;

import com.dacarex.capital.db.ConexionBD;
import com.dacarex.capital.enums.TipoCuenta;
import com.dacarex.capital.exception.EntidadNoEncontradaException;
import com.dacarex.capital.exception.PersistenciaException;
import com.dacarex.capital.io.Logger;
import com.dacarex.capital.model.PreferenciasUsuario;
import com.dacarex.capital.model.Usuario;
import com.dacarex.capital.service.IRepositorio;

import java.sql.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class UsuarioDAO implements IRepositorio<Usuario, String> {

    private Connection getConexion() {
        return ConexionBD.getInstance().getConexion();
    }

    @Override
    public void guardar(Usuario u) {
        String sql = """
            INSERT INTO usuarios
            (id, nombre_completo, email, contrasenia, tipo_cuenta, nombre_empresa, creado_en, actualizado_en)
            VALUES (?, ?, ?, ?, ?, ?, ?, ?)
            """;
        try (PreparedStatement ps = getConexion().prepareStatement(sql)) {
            ps.setString(1, u.getId());
            ps.setString(2, u.getNombreCompleto());
            ps.setString(3, u.getEmail());
            ps.setString(4, u.getContrasenia());
            ps.setString(5, u.getTipoCuenta().name());
            ps.setString(6, u.getNombreEmpresa());
            ps.setTimestamp(7, Timestamp.valueOf(u.getCreadoEn()));
            ps.setTimestamp(8, Timestamp.valueOf(u.getActualizadoEn()));
            ps.executeUpdate();
            Logger.info("Usuario guardado: " + u.getEmail());
        } catch (SQLException e) {
            Logger.error("Error al guardar usuario: " + e.getMessage());
            throw new PersistenciaException("No se pudo guardar el usuario.", e);
        }
    }

    @Override
    public Optional<Usuario> buscarPorId(String id) {
        String sql = "SELECT * FROM usuarios WHERE id = ?";
        try (PreparedStatement ps = getConexion().prepareStatement(sql)) {
            ps.setString(1, id);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) return Optional.of(mapear(rs));
        } catch (SQLException e) {
            Logger.error("Error al buscar usuario: " + e.getMessage());
            throw new PersistenciaException("Error al buscar usuario.", e);
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
            Logger.error("Error al buscar usuario por email: " + e.getMessage());
            throw new PersistenciaException("Error al buscar usuario.", e);
        }
        return Optional.empty();
    }

    @Override
    public List<Usuario> buscarTodos() {
        List<Usuario> lista = new ArrayList<>();
        String sql = "SELECT * FROM usuarios ORDER BY nombre_completo";
        try (Statement st = getConexion().createStatement();
             ResultSet rs = st.executeQuery(sql)) {
            while (rs.next()) lista.add(mapear(rs));
        } catch (SQLException e) {
            Logger.error("Error al listar usuarios: " + e.getMessage());
            throw new PersistenciaException("Error al listar usuarios.", e);
        }
        return lista;
    }

    @Override
    public void actualizar(Usuario u) {
        String sql = """
            UPDATE usuarios SET
            nombre_completo = ?, email = ?, contrasenia = ?,
            tipo_cuenta = ?, nombre_empresa = ?, actualizado_en = ?
            WHERE id = ?
            """;
        try (PreparedStatement ps = getConexion().prepareStatement(sql)) {
            ps.setString(1, u.getNombreCompleto());
            ps.setString(2, u.getEmail());
            ps.setString(3, u.getContrasenia());
            ps.setString(4, u.getTipoCuenta().name());
            ps.setString(5, u.getNombreEmpresa());
            ps.setTimestamp(6, Timestamp.valueOf(LocalDateTime.now()));
            ps.setString(7, u.getId());
            ps.executeUpdate();
            Logger.info("Usuario actualizado: " + u.getEmail());
        } catch (SQLException e) {
            Logger.error("Error al actualizar usuario: " + e.getMessage());
            throw new PersistenciaException("No se pudo actualizar el usuario.", e);
        }
    }

    @Override
    public void eliminar(String id) {
        String sql = "DELETE FROM usuarios WHERE id = ?";
        try (PreparedStatement ps = getConexion().prepareStatement(sql)) {
            ps.setString(1, id);
            ps.executeUpdate();
            Logger.info("Usuario eliminado: " + id);
        } catch (SQLException e) {
            Logger.error("Error al eliminar usuario: " + e.getMessage());
            throw new PersistenciaException("No se pudo eliminar el usuario.", e);
        }
    }

    private Usuario mapear(ResultSet rs) throws SQLException {
        return new Usuario(
            rs.getString("id"),
            rs.getString("nombre_completo"),
            rs.getString("email"),
            rs.getString("contrasenia"),
            TipoCuenta.valueOf(rs.getString("tipo_cuenta")),
            rs.getString("nombre_empresa"),
            new PreferenciasUsuario()
        );
    }
}