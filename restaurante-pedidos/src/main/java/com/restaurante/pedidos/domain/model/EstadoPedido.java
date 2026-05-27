package com.restaurante.pedidos.domain.model;

public enum EstadoPedido {
    EN_CREACION,
    PENDIENTE_CONFIRMACION, // Esperando que el restaurante valide disponibilidad
    ESPERANDO_PAGO,         // Pedido aceptado, esperando que el cliente transfiera y mande foto
    PENDIENTE_VALIDACION_PAGO, // Foto recibida, validando el SPEI con Banxico/Staff
    EN_COCINA,
    LISTO,
    CANCELADO
}