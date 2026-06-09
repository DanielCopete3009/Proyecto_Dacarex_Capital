package com.dacarex.capital.service;

import java.util.List;
import java.util.Optional;

public interface IRepositorio<T, ID> {

    void guardar(T entidad);

    Optional<T> buscarPorId(ID id);

    List<T> buscarTodos();

    void actualizar(T entidad);

    void eliminar(ID id);
}