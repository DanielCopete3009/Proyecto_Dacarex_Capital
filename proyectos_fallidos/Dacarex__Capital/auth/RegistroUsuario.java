package com.dacarex.capital.auth;

import com.dacarex.capital.enums.TipoCuenta;
import com.dacarex.capital.model.IValidable;

import java.util.ArrayList;
import java.util.List;

public class RegistroUsuario implements IValidable {

    private String nombreCompleto;
    private String email;
    private String contrasenia;
    private String confirmarContrasenia;
    private TipoCuenta tipoCuenta;
    private String nombreEmpresa;

    public RegistroUsuario(String nombreCompleto, String email, String contrasenia,
                           String confirmarContrasenia, TipoCuenta tipoCuenta, String nombreEmpresa) {
        this.nombreCompleto = nombreCompleto;
        this.email = email;
        this.contrasenia = contrasenia;
        this.confirmarContrasenia = confirmarContrasenia;
        this.tipoCuenta = tipoCuenta;
        this.nombreEmpresa = nombreEmpresa;
    }

    @Override
    public List<String> validar() {
        List<String> errores = new ArrayList<>();
        if (nombreCompleto == null || nombreCompleto.isBlank())
            errores.add("El nombre completo es obligatorio.");
        if (email == null || !email.contains("@"))
            errores.add("El email no es válido.");
        if (contrasenia == null || contrasenia.length() < 8)
            errores.add("La contraseña debe tener al menos 8 caracteres.");
        if (!java.util.Objects.equals(contrasenia, confirmarContrasenia))
            errores.add("Las contraseñas no coinciden.");
        if (TipoCuenta.EMPRESA.equals(tipoCuenta) && (nombreEmpresa == null || nombreEmpresa.isBlank()))
            errores.add("El nombre de empresa es obligatorio para cuentas de empresa.");
        return errores;
    }

    // Getters y Setters
    public String getNombreCompleto() { return nombreCompleto; }
    public void setNombreCompleto(String nombreCompleto) { this.nombreCompleto = nombreCompleto; }

    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }

    public String getContrasenia() { return contrasenia; }
    public void setContrasenia(String contrasenia) { this.contrasenia = contrasenia; }

    public String getConfirmarContrasenia() { return confirmarContrasenia; }
    public void setConfirmarContrasenia(String confirmarContrasenia) { this.confirmarContrasenia = confirmarContrasenia; }

    public TipoCuenta getTipoCuenta() { return tipoCuenta; }
    public void setTipoCuenta(TipoCuenta tipoCuenta) { this.tipoCuenta = tipoCuenta; }

    public String getNombreEmpresa() { return nombreEmpresa; }
    public void setNombreEmpresa(String nombreEmpresa) { this.nombreEmpresa = nombreEmpresa; }
}