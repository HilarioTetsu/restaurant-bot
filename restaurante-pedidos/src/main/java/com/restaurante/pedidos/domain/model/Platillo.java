package com.restaurante.pedidos.domain.model;

import java.math.BigDecimal;

public class Platillo {
    private final Long id;
    private final String nombre;
    private final BigDecimal precio;
    private final boolean disponible;

    public Platillo(Long id, String nombre, BigDecimal precio, boolean disponible) {
        this.id = id;
        this.nombre = nombre;
        this.precio = precio;
        this.disponible = disponible;
    }

    public Long getId() { return id; }
    public String getNombre() { return nombre; }
    public BigDecimal getPrecio() { return precio; }
    public boolean isDisponible() { return disponible; }
}