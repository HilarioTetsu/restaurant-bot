package com.restaurante.pedidos.application.usecases;

import com.restaurante.pedidos.application.ports.in.IMarcarPedidoListoPort;
import com.restaurante.pedidos.application.ports.out.IPedidoRepository;
import com.restaurante.pedidos.domain.model.Pedido;

public class MarcarPedidoListoUseCase implements IMarcarPedidoListoPort {

    private final IPedidoRepository pedidoRepository;

    public MarcarPedidoListoUseCase(IPedidoRepository pedidoRepository) {
        this.pedidoRepository = pedidoRepository;
    }

    @Override
    public Pedido ejecutar(String pedidoId) {
        Pedido pedido = pedidoRepository.buscarPorId(pedidoId)
                .orElseThrow(() -> new IllegalArgumentException("El pedido con ID " + pedidoId + " no existe."));

        pedido.marcarComoListo();
        pedidoRepository.guardar(pedido);
        return pedido;
    }
}