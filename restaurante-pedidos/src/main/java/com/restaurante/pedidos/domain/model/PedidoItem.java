package com.restaurante.pedidos.domain.model;

import java.math.BigDecimal;

public class PedidoItem {
    private Long id;
    private final Platillo platillo;
    private int cantidad;
    private final BigDecimal precioUnitario;

    // El constructor recibe el platillo y la cantidad inicial
    public PedidoItem(Platillo platillo, int cantidad) {
        if (cantidad <= 0) {
            throw new IllegalArgumentException("La cantidad debe ser mayor a cero.");
        }
        this.platillo = platillo;
        this.cantidad = cantidad;
        this.precioUnitario = platillo.getPrecio(); // Congela el precio histórico
    }

    public void incrementarCantidad(int cantidadAAgregar) {
        if (cantidadAAgregar <= 0) {
            throw new IllegalArgumentException("La cantidad a agregar debe ser mayor a cero.");
        }
        this.cantidad += cantidadAAgregar;
    }

    public BigDecimal getSubtotal() {
        return this.precioUnitario.multiply(BigDecimal.valueOf(this.cantidad));
    }

    // Getters puros
    public Long getId() { return id; }
    public Platillo getPlatillo() { return platillo; }
    public int getCantidad() { return cantidad; }
    public BigDecimal getPrecioUnitario() { return precioUnitario; }
    public void setId(Long id) {
        this.id = id;
    }
}