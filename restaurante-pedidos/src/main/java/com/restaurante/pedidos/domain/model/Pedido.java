package com.restaurante.pedidos.domain.model;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class Pedido {
    private final String id; // UUID de la sesión del bot
    private final String telefonoCliente;
    private EstadoPedido estado;
    private BigDecimal total;
    private final LocalDateTime fechaCreacion;
    private final List<Platillo> items;

    // Constructor para inicializar un pedido nuevo desde el bot
    public Pedido(String id, String telefonoCliente) {
        this.id = id;
        this.telefonoCliente = telefonoCliente;
        this.estado = EstadoPedido.EN_CREACION;
        this.total = BigDecimal.ZERO;
        this.fechaCreacion = LocalDateTime.now();
        this.items = new ArrayList<>();
    }
    
    public Pedido(String id, String telefonoCliente, EstadoPedido estado, BigDecimal total, LocalDateTime fechaCreacion) {
        this.id = id;
        this.telefonoCliente = telefonoCliente;
        this.estado = estado;
        this.total = total;
        this.fechaCreacion = fechaCreacion;
        this.items = new ArrayList<>(); // Los detalles de relación se poblarán en fases posteriores
    }

    // Regla: Añadir comida al carrito
    public void agregarPlatillo(Platillo platillo) {
        if (this.estado != EstadoPedido.EN_CREACION) {
            throw new IllegalStateException("No puedes modificar un pedido que ya fue enviado para revision.");
        }
        if (!platillo.isDisponible()) {
            throw new IllegalArgumentException("El platillo '" + platillo.getNombre() + "' no esta disponible hoy.");
        }
        this.items.add(platillo);
        this.total = this.total.add(platillo.getPrecio());
    }

    // Transición 1: Cliente termina de elegir y solicita confirmación del negocio
    public void solicitarConfirmacion() {
        if (this.items.isEmpty()) {
            throw new IllegalStateException("El carrito de compras no puede estar vacio.");
        }
        if (this.estado != EstadoPedido.EN_CREACION) {
            throw new IllegalStateException("El pedido no se encuentra en estado de creacion.");
        }
        this.estado = EstadoPedido.PENDIENTE_CONFIRMACION;
    }

    // Transición 2: El restaurante da el primer check (Viabilidad técnica/insumos)
    public void confirmarPorNegocio() {
        if (this.estado != EstadoPedido.PENDIENTE_CONFIRMACION) {
            throw new IllegalStateException("Solo se pueden confirmar pedidos que estén pendientes de revision.");
        }
        this.estado = EstadoPedido.ESPERANDO_PAGO;
    }

    // Transición 3: El cliente envía la captura de pantalla por WhatsApp
    public void recibirComprobantePago() {
        if (this.estado != EstadoPedido.ESPERANDO_PAGO) {
            throw new IllegalStateException("El bot no esta esperando un pago para este pedido.");
        }
        this.estado = EstadoPedido.PENDIENTE_VALIDACION_PAGO;
    }

    // Transición 4: El restaurante da el segundo check (Financiero / OCR / Banxico)
    public void aprobarPagoParaCocina() {
        if (this.estado != EstadoPedido.PENDIENTE_VALIDACION_PAGO) {
            throw new IllegalStateException("No se puede pasar a cocina un pedido sin comprobante de pago recibido.");
        }
        this.estado = EstadoPedido.EN_COCINA;
    }

    // Transición 5: El cocinero termina el pedido
    public void marcarComoListo() {
        if (this.estado != EstadoPedido.EN_COCINA) {
            throw new IllegalStateException("Solo los pedidos en preparacion de cocina pueden marcarse como listos.");
        }
        this.estado = EstadoPedido.LISTO;
    }

    // Cancelación global aplicable a los estados iniciales
    public void cancelar(String motivo) {
        if (this.estado == EstadoPedido.EN_COCINA || this.estado == EstadoPedido.LISTO) {
            throw new IllegalStateException("No se puede cancelar un pedido que ya esta en preparacion o terminado.");
        }
        this.estado = EstadoPedido.CANCELADO;
    }

    
    public String getId() { return id; }
    public String getTelefonoCliente() { return telefonoCliente; }
    public EstadoPedido getEstado() { return estado; }
    public BigDecimal getTotal() { return total; }
    public LocalDateTime getFechaCreacion() { return fechaCreacion; }
    public List<Platillo> getItems() { return new ArrayList<>(items); }
}