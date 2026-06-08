package com.dacarex.capital.service;

import com.dacarex.capital.auth.CredencialesLogin;
import com.dacarex.capital.auth.RegistroUsuario;
import com.dacarex.capital.auth.SesionUsuario;
import com.dacarex.capital.enums.TipoCuenta;
import com.dacarex.capital.exception.AutenticacionException;
import com.dacarex.capital.exception.ValidacionException;
import com.dacarex.capital.model.PreferenciasUsuario;
import com.dacarex.capital.model.Usuario;

import java.time.LocalDateTime;
import java.util.*;

public class AuthService {

    // Set para emails únicos
    private final Map<String, Usuario> usuariosPorEmail = new HashMap<>();
    private SesionUsuario sesionActiva = null;

    public Usuario registrar(RegistroUsuario form) {
        if (!form.esValido())
            throw new ValidacionException(form.validar());

        if (usuariosPorEmail.containsKey(form.getEmail().toLowerCase()))
            throw new ValidacionException(List.of("Ya existe una cuenta con ese email."));

        String id = "usr-" + UUID.randomUUID().toString().substring(0, 8);
        Usuario nuevo = new Usuario(
            id,
            form.getNombreCompleto(),
            form.getEmail(),
            form.getContrasenia(),
            form.getTipoCuenta(),
            form.getNombreEmpresa(),
            new PreferenciasUsuario()
        );
        usuariosPorEmail.put(form.getEmail().toLowerCase(), nuevo);
        return nuevo;
    }

    public SesionUsuario login(CredencialesLogin credenciales) {
        if (!credenciales.esValido())
            throw new ValidacionException(credenciales.validar());

        Usuario usuario = usuariosPorEmail.get(
            credenciales.getUsuarioOEmail().toLowerCase()
        );

        if (usuario == null || !usuario.getContrasenia().equals(credenciales.getContrasenia()))
            throw new AutenticacionException("Email o contraseña incorrectos.");

        String token = UUID.randomUUID().toString();
        sesionActiva = new SesionUsuario(usuario, token, LocalDateTime.now().plusHours(8));
        return sesionActiva;
    }

    public void logout() {
        if (sesionActiva != null) {
            sesionActiva.cerrar();
            sesionActiva = null;
        }
    }

    public SesionUsuario getSesionActiva() {
        return sesionActiva;
    }

    public boolean haySesionActiva() {
        return sesionActiva != null && sesionActiva.estaActiva();
    }

    public Usuario getUsuarioActual() {
        if (!haySesionActiva())
            throw new AutenticacionException("No hay ninguna sesión activa.");
        return sesionActiva.getUsuario();
    }

    // Para pruebas: crea un usuario administrador por defecto
    public void crearUsuarioDemoSiVacio() {
        if (usuariosPorEmail.isEmpty()) {
            String id = "usr-demo";
            Usuario demo = new Usuario(
                id, "Usuario Demo", "demo@dacarex.com",
                "demo1234", TipoCuenta.PERSONAL, null,
                new PreferenciasUsuario()
            );
            usuariosPorEmail.put("demo@dacarex.com", demo);
        }
    }
}