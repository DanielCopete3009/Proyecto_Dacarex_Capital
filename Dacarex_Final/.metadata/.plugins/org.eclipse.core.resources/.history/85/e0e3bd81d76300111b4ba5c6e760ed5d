package com.dacarex.capital.modelo;

import javax.persistence.*;

@MappedSuperclass
public abstract class EntidadBase {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private long id;

    public long getId() { return id; }
    public void setId(long id) { this.id = id; }

    public abstract String toResumen();

    @Override
    public String toString() {
        return toResumen();
    }
}