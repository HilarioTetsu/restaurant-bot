package com.restaurante.pedidos.application.ports.in;

import com.restaurante.pedidos.domain.model.Pedido;

public interface ISolicitarConfirmacionPort {
    Pedido ejecutar(String pedidoId);
}