package com.dacarex.capital.exception;

import java.util.List;

public class ValidacionException extends DacarexException {

    private final List<String> errores;

    public ValidacionException(List<String> errores) {
        super("Error de validación: " + String.join(", ", errores));
        this.errores = errores;
    }

    public List<String> getErrores() {
        return errores;
    }
}