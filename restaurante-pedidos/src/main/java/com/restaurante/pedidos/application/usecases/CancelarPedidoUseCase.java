package com.restaurante.pedidos.application.usecases;

import com.restaurante.pedidos.application.ports.in.ICancelarPedidoPort;
import com.restaurante.pedidos.application.ports.out.IPedidoRepository;
import com.restaurante.pedidos.domain.model.Pedido;

public class CancelarPedidoUseCase implements ICancelarPedidoPort {

    private final IPedidoRepository pedidoRepository;

    public CancelarPedidoUseCase(IPedidoRepository pedidoRepository) {
        this.pedidoRepository = pedidoRepository;
    }

    @Override
    public Pedido ejecutar(String pedidoId, String motivo) {
        Pedido pedido = pedidoRepository.buscarPorId(pedidoId)
                .orElseThrow(() -> new IllegalArgumentException("El pedido con ID " + pedidoId + " no existe."));

        pedido.cancelar(motivo);
        pedidoRepository.guardar(pedido);
        return pedido;
    }
}