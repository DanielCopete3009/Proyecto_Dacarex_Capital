package com.dacarex.capital.dao;

import com.dacarex.capital.modelo.Movimiento;
import com.dacarex.capital.modelo.TipoMovimiento;

import javax.persistence.TypedQuery;
import java.util.List;
import java.util.Optional;

// Hereda de DAOBase para poder usar operaciones comunes como guardar() o actualizar() con Movimientos
public class MovimientoDAO extends DAOBase<Movimiento> {

    // Devuelve todos los movimientos de la base de datos, del más reciente al más antiguo
    @Override
    public List<Movimiento> buscarTodos() {
        TypedQuery<Movimiento> q = getEm().createQuery(
            "SELECT m FROM Movimiento m ORDER BY m.fecha DESC",
            Movimiento.class
        );
        return q.getResultList();
    }

    // Filtra los movimientos por tipo (INGRESO o GASTO) y los ordena por fecha reciente
    public List<Movimiento> buscarPorTipo(TipoMovimiento tipo) {
        TypedQuery<Movimiento> q = getEm().createQuery(
            "SELECT m FROM Movimiento m WHERE m.tipo = :tipo ORDER BY m.fecha DESC",
            Movimiento.class
        );
        q.setParameter("tipo", tipo); // Inyecta el parámetro de forma segura
        return q.getResultList();
    }

    // Busca movimientos que contengan una palabra o texto específico en su descripción (sin importar mayúsculas)
    public List<Movimiento> buscarPorTexto(String texto) {
        TypedQuery<Movimiento> q = getEm().createQuery(
            "SELECT m FROM Movimiento m WHERE LOWER(m.descripcion) LIKE :texto ORDER BY m.fecha DESC",
            Movimiento.class
        );
        // El "%" a los lados sirve para que busque el texto en cualquier parte de la frase
        q.setParameter("texto", "%" + texto.toLowerCase() + "%");
        return q.getResultList();
    }

    // Busca un movimiento específico usando su número de ID único
    public Optional<Movimiento> buscarPorId(long id) {
        return buscarPorId(id, Movimiento.class);
    }

    // Borra un movimiento de la base de datos pasándole su ID
    public void eliminar(long id) {
        eliminar(id, Movimiento.class);
    }

    // Suma el dinero de todos los registros que sean de tipo INGRESO
    public double calcularTotalIngresos() {
        TypedQuery<Double> q = getEm().createQuery(
            "SELECT SUM(m.importe) FROM Movimiento m WHERE m.tipo = :tipo",
            Double.class
        );
        q.setParameter("tipo", TipoMovimiento.INGRESO);
        Double resultado = q.getSingleResult(); // Obtiene el resultado de la suma
        return resultado != null ? resultado : 0.0; // Si no hay ingresos, devuelve 0.0 para evitar errores
    }

    // Suma el dinero de todos los registros que sean de tipo GASTO
    public double calcularTotalGastos() {
        TypedQuery<Double> q = getEm().createQuery(
            "SELECT SUM(m.importe) FROM Movimiento m WHERE m.tipo = :tipo",
            Double.class
        );
        q.setParameter("tipo", TipoMovimiento.GASTO);
        Double resultado = q.getSingleResult(); // Obtiene el resultado de la suma
        return resultado != null ? resultado : 0.0; // Si no hay gastos, devuelve 0.0 de forma segura
    }
}