package com.restaurante.pedidos.application.ports.in;

import com.restaurante.pedidos.domain.model.Pedido;

public interface ICancelarPedidoPort {
    Pedido ejecutar(String pedidoId, String motivo);
}