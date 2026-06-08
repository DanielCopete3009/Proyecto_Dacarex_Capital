package com.dacarex.capital.exception;

public class EntidadNoEncontradaException extends DacarexException {

    public EntidadNoEncontradaException(String entidad, String id) {
        super("No se encontró " + entidad + " con id: " + id);
    }
}