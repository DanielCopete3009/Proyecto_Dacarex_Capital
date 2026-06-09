package com.dacarex.capital.model;

import java.util.List;

public interface IValidable {

    List<String> validar();

    default boolean esValido() {
        return validar().isEmpty();
    }
}