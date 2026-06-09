package com.dacarex.capital.modelo;

import javax.persistence.*;

// @MappedSuperclass le dice a JPA que esta clase no será una tabla en sí misma,
// sino que compartirá sus columnas (como el id) con todas las clases que la hereden (Categoria, Usuario, etc.).
@MappedSuperclass
public abstract class EntidadBase {

    // @Id marca esta variable como la clave primaria (ID único) en la base de datos.
    // @GeneratedValue indica que ObjectDB generará el número de ID automáticamente (1, 2, 3...) al guardar.
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private long id;

    // Métodos Getter y Setter para leer y modificar el ID único
    public long getId() { return id; }
    public void setId(long id) { this.id = id; }

    // Método abstracto: no tiene código aquí dentro. Obliga a todas las clases hijas
    // a escribir su propio método para devolver un resumen de sus datos en formato texto.
    public abstract String toResumen();

    // Sobreescribe el método estándar de Java para que, cuando se intente imprimir el objeto,
    // use automáticamente el texto descriptivo del método "toResumen()".
    @Override
    public String toString() {
        return toResumen();
    }
}