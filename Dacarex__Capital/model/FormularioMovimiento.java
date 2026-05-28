package com.dacarex.capital.model;

import com.dacarex.capital.enums.TipoMovimiento;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class FormularioMovimiento implements IValidable {

    private TipoMovimiento tipo;
    private String descripcion;
    private Double importe;
    private String categoriaId;
    private LocalDate fecha;
    private String notas;

    public FormularioMovimiento() {
        this.tipo = TipoMovimiento.GASTO;
        this.descripcion = "";
        this.importe = null;
        this.categoriaId = "";
        this.fecha = null;
    }

    public FormularioMovimiento(TipoMovimiento tipo, String descripcion, Double importe,
                                String categoriaId, LocalDate fecha, String notas) {
        this.tipo = tipo;
        this.descripcion = descripcion;
        this.importe = importe;
        this.categoriaId = categoriaId;
        this.fecha = fecha;
        this.notas = notas;
    }

    @Override
    public List<String> validar() {
        List<String> errores = new ArrayList<>();
        if (descripcion == null || descripcion.isBlank())
            errores.add("La descripción es obligatoria.");
        if (importe == null || importe <= 0)
            errores.add("El importe debe ser mayor que 0.");
        if (categoriaId == null || categoriaId.isBlank())
            errores.add("La categoría es obligatoria.");
        if (fecha == null)
            errores.add("La fecha es obligatoria.");
        return errores;
    }

    // Getters y Setters
    public TipoMovimiento getTipo() { return tipo; }
    public void setTipo(TipoMovimiento tipo) { this.tipo = tipo; }

    public String getDescripcion() { return descripcion; }
    public void setDescripcion(String descripcion) { this.descripcion = descripcion; }

    public Double getImporte() { return importe; }
    public void setImporte(Double importe) { this.importe = importe; }

    public String getCategoriaId() { return categoriaId; }
    public void setCategoriaId(String categoriaId) { this.categoriaId = categoriaId; }

    public LocalDate getFecha() { return fecha; }
    public void setFecha(LocalDate fecha) { this.fecha = fecha; }

    public String getNotas() { return notas; }
    public void setNotas(String notas) { this.notas = notas; }
}