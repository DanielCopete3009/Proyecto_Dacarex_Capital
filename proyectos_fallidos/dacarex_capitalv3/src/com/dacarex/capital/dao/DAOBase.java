package com.dacarex.capital.dao;

import java.sql.Connection;
import java.util.List;
import java.util.Optional;

public abstract class DAOBase<T> {

    // Todos los DAOs tienen acceso a la conexión
    protected Connection getConexion() {
        return ConexionBD.getInstance().getConexion();
    }

    // Contrato que deben cumplir todos los DAOs
    public abstract void guardar(T entidad);
    public abstract Optional<T> buscarPorId(int id);
    public abstract List<T> buscarTodos();
    public abstract void actualizar(T entidad);
    public abstract void eliminar(int id);
}