package com.restaurante.pedidos.application.ports.in;

import com.restaurante.pedidos.domain.model.Pedido;

public interface IAgregarPlatilloPort {
	Pedido ejecutar(String pedidoId, String telefonoCliente, Long platilloId);
}
