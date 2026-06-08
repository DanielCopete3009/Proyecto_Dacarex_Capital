package com.dacarex.capital.service;

import com.dacarex.capital.enums.TipoMovimiento;
import com.dacarex.capital.exception.EntidadNoEncontradaException;
import com.dacarex.capital.exception.ValidacionException;
import com.dacarex.capital.model.Categoria;
import com.dacarex.capital.model.FormularioCategoria;

import java.util.*;
import java.util.stream.Collectors;

public class CategoriaService {

    // Map para acceso O(1) por id
    private final Map<String, Categoria> categorias = new LinkedHashMap<>();

    public Categoria crear(FormularioCategoria form) {
        if (!form.esValido())
            throw new ValidacionException(form.validar());

        boolean nombreDuplicado = categorias.values().stream()
                .anyMatch(c -> c.getNombre().equalsIgnoreCase(form.getNombre())
                            && c.getTipo() == form.getTipo());
        if (nombreDuplicado)
            throw new ValidacionException(
                List.of("Ya existe una categoría con ese nombre y tipo.")
            );

        String id = "cat-" + UUID.randomUUID().toString().substring(0, 8);
        Categoria nueva = new Categoria(id, form.getNombre(), form.getTipo());
        categorias.put(id, nueva);
        return nueva;
    }

    public Categoria buscarPorId(String id) {
        return Optional.ofNullable(categorias.get(id))
                .orElseThrow(() -> new EntidadNoEncontradaException("Categoría", id));
    }

    public List<Categoria> buscarTodas() {
        return new ArrayList<>(categorias.values());
    }

    public List<Categoria> buscarPorTipo(TipoMovimiento tipo) {
        return categorias.values().stream()
                .filter(c -> c.getTipo() == tipo)
                .sorted(Comparator.comparing(Categoria::getNombre))
                .collect(Collectors.toList());
    }

    public Categoria actualizar(String id, FormularioCategoria form) {
        if (!form.esValido())
            throw new ValidacionException(form.validar());

        Categoria existente = buscarPorId(id);
        existente.setNombre(form.getNombre());
        existente.setTipo(form.getTipo());
        existente.marcarActualizado();
        return existente;
    }

    public void eliminar(String id) {
        Categoria categoria = buscarPorId(id);
        if (categoria.isEnUso())
            throw new ValidacionException(
                List.of("No se puede eliminar una categoría que tiene movimientos asociados.")
            );
        categorias.remove(id);
    }

    public void marcarEnUso(String id) {
        buscarPorId(id).setEnUso(true);
    }

    public int contarPorTipo(TipoMovimiento tipo) {
        return (int) categorias.values().stream()
                .filter(c -> c.getTipo() == tipo)
                .count();
    }

    public void cargarCategoriasIniciales() {
        List<FormularioCategoria> iniciales = Arrays.asList(
            new FormularioCategoria("Nómina",       TipoMovimiento.INGRESO),
            new FormularioCategoria("Ventas",        TipoMovimiento.INGRESO),
            new FormularioCategoria("Alquiler",      TipoMovimiento.GASTO),
            new FormularioCategoria("Suministros",   TipoMovimiento.GASTO),
            new FormularioCategoria("Marketing",     TipoMovimiento.GASTO),
            new FormularioCategoria("Otros",         TipoMovimiento.GASTO)
        );
        iniciales.forEach(this::crear);
    }
}