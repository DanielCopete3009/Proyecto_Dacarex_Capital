package com.dacarex.capital.dao;

import javax.persistence.EntityManager;
import java.util.List;
import java.util.Optional;

public abstract class DAOBase<T> {

    protected EntityManager getEm() {
        return ConexionDB.getInstance().getEm();
    }

    public void guardar(T entidad) {
        EntityManager em = getEm();
        em.getTransaction().begin();
        em.persist(entidad);
        em.getTransaction().commit();
    }

    public void actualizar(T entidad) {
        EntityManager em = getEm();
        em.getTransaction().begin();
        em.merge(entidad);
        em.getTransaction().commit();
    }

    public void eliminar(Object id, Class<T> clase) {
        EntityManager em = getEm();
        T entidad = em.find(clase, id);
        if (entidad != null) {
            em.getTransaction().begin();
            em.remove(entidad);
            em.getTransaction().commit();
        }
    }

    public Optional<T> buscarPorId(Object id, Class<T> clase) {
        return Optional.ofNullable(getEm().find(clase, id));
    }

    public abstract List<T> buscarTodos();
}