package com.dacarex.capital.dao;

import com.dacarex.capital.modelo.Categoria;
import com.dacarex.capital.modelo.TipoMovimiento;

import javax.persistence.TypedQuery;
import java.util.List;
import java.util.Optional;

public class CategoriaDAO extends DAOBase<Categoria> {

    @Override
    public List<Categoria> buscarTodos() {
        TypedQuery<Categoria> q = getEm().createQuery(
            "SELECT c FROM Categoria c ORDER BY c.nombre",
            Categoria.class
        );
        return q.getResultList();
    }

    public List<Categoria> buscarPorTipo(TipoMovimiento tipo) {
        TypedQuery<Categoria> q = getEm().createQuery(
            "SELECT c FROM Categoria c WHERE c.tipo = :tipo ORDER BY c.nombre",
            Categoria.class
        );
        q.setParameter("tipo", tipo);
        return q.getResultList();
    }

    public Optional<Categoria> buscarPorId(long id) {
        return buscarPorId(id, Categoria.class);
    }

    public void eliminar(long id) {
        eliminar(id, Categoria.class);
    }

    public void cargarIniciales() {
        if (buscarTodos().isEmpty()) {
            guardar(new Categoria("Nomina",      TipoMovimiento.INGRESO));
            guardar(new Categoria("Ventas",      TipoMovimiento.INGRESO));
            guardar(new Categoria("Alquiler",    TipoMovimiento.GASTO));
            guardar(new Categoria("Suministros", TipoMovimiento.GASTO));
            guardar(new Categoria("Marketing",   TipoMovimiento.GASTO));
            guardar(new Categoria("Otros",       TipoMovimiento.GASTO));
            System.out.println("Categorias iniciales cargadas.");
        }
    }
}