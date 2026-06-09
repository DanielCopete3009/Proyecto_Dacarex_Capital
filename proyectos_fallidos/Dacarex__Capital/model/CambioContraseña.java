package com.dacarex.capital.model;

import java.util.ArrayList;
import java.util.List;

public class CambioContrasenia implements IValidable {

    private String contraseniaActual;
    private String nuevaContrasenia;
    private String confirmarNuevaContrasenia;

    public CambioContrasenia(String contraseniaActual, String nuevaContrasenia,
                             String confirmarNuevaContrasenia) {
        this.contraseniaActual = contraseniaActual;
        this.nuevaContrasenia = nuevaContrasenia;
        this.confirmarNuevaContrasenia = confirmarNuevaContrasenia;
    }

    @Override
    public List<String> validar() {
        List<String> errores = new ArrayList<>();
        if (contraseniaActual == null || contraseniaActual.isEmpty())
            errores.add("La contraseña actual es obligatoria.");
        if (nuevaContrasenia == null || nuevaContrasenia.length() < 8)
            errores.add("La nueva contraseña debe tener al menos 8 caracteres.");
        if (!java.util.Objects.equals(nuevaContrasenia, confirmarNuevaContrasenia))
            errores.add("Las contraseñas no coinciden.");
        return errores;
    }

    // Getters y Setters
    public String getContraseniaActual() { return contraseniaActual; }
    public void setContraseniaActual(String c) { this.contraseniaActual = c; }

    public String getNuevaContrasenia() { return nuevaContrasenia; }
    public void setNuevaContrasenia(String c) { this.nuevaContrasenia = c; }

    public String getConfirmarNuevaContrasenia() { return confirmarNuevaContrasenia; }
    public void setConfirmarNuevaContrasenia(String c) { this.confirmarNuevaContrasenia = c; }
}