package com.restaurante.pedidos.application.ports.in;

import com.restaurante.pedidos.domain.model.Pedido;

public interface IAprobarPagoPort {
    Pedido ejecutar(String pedidoId);
}