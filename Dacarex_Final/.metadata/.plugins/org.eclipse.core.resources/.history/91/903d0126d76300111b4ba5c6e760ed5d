package com.dacarex.capital.dao;

import com.dacarex.capital.modelo.Movimiento;
import com.dacarex.capital.modelo.TipoMovimiento;

import javax.persistence.TypedQuery;
import java.util.List;
import java.util.Optional;

public class MovimientoDAO extends DAOBase<Movimiento> {

    @Override
    public List<Movimiento> buscarTodos() {
        TypedQuery<Movimiento> q = getEm().createQuery(
            "SELECT m FROM Movimiento m ORDER BY m.fecha DESC",
            Movimiento.class
        );
        return q.getResultList();
    }

    public List<Movimiento> buscarPorTipo(TipoMovimiento tipo) {
        TypedQuery<Movimiento> q = getEm().createQuery(
            "SELECT m FROM Movimiento m WHERE m.tipo = :tipo ORDER BY m.fecha DESC",
            Movimiento.class
        );
        q.setParameter("tipo", tipo);
        return q.getResultList();
    }

    public List<Movimiento> buscarPorTexto(String texto) {
        TypedQuery<Movimiento> q = getEm().createQuery(
            "SELECT m FROM Movimiento m WHERE LOWER(m.descripcion) LIKE :texto ORDER BY m.fecha DESC",
            Movimiento.class
        );
        q.setParameter("texto", "%" + texto.toLowerCase() + "%");
        return q.getResultList();
    }

    public Optional<Movimiento> buscarPorId(long id) {
        return buscarPorId(id, Movimiento.class);
    }

    public void eliminar(long id) {
        eliminar(id, Movimiento.class);
    }

    public double calcularTotalIngresos() {
        TypedQuery<Double> q = getEm().createQuery(
            "SELECT SUM(m.importe) FROM Movimiento m WHERE m.tipo = :tipo",
            Double.class
        );
        q.setParameter("tipo", TipoMovimiento.INGRESO);
        Double resultado = q.getSingleResult();
        return resultado != null ? resultado : 0.0;
    }

    public double calcularTotalGastos() {
        TypedQuery<Double> q = getEm().createQuery(
            "SELECT SUM(m.importe) FROM Movimiento m WHERE m.tipo = :tipo",
            Double.class
        );
        q.setParameter("tipo", TipoMovimiento.GASTO);
        Double resultado = q.getSingleResult();
        return resultado != null ? resultado : 0.0;
    }
}