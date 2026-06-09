package com.dacarex.capital.dao;

import com.dacarex.capital.modelo.Categoria;
import com.dacarex.capital.modelo.TipoMovimiento;

import javax.persistence.TypedQuery;
import java.util.List;
import java.util.Optional;

// Esta clase hereda de DAOBase para heredar métodos como guardar() o getEm()
public class CategoriaDAO extends DAOBase<Categoria> {

    // Obtiene todas las categorías de la base de datos ordenadas por nombre
    @Override
    public List<Categoria> buscarTodos() {
        TypedQuery<Categoria> q = getEm().createQuery(
            "SELECT c FROM Categoria c ORDER BY c.nombre",
            Categoria.class
        );
        return q.getResultList();
    }

    // Filtra y devuelve las categorías según su tipo (INGRESO o GASTO)
    public List<Categoria> buscarPorTipo(TipoMovimiento tipo) {
        TypedQuery<Categoria> q = getEm().createQuery(
            "SELECT c FROM Categoria c WHERE c.tipo = :tipo ORDER BY c.nombre",
            Categoria.class
        );
        q.setParameter("tipo", tipo); // Asigna el parámetro a la consulta
        return q.getResultList();
    }

    // Busca una sola categoría por su número de ID único
    public Optional<Categoria> buscarPorId(long id) {
        return buscarPorId(id, Categoria.class);
    }

    // Borra una categoría de la base de datos usando su ID
    public void eliminar(long id) {
        eliminar(id, Categoria.class);
    }

    // Si la base de datos está vacía, mete categorías por defecto para empezar
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