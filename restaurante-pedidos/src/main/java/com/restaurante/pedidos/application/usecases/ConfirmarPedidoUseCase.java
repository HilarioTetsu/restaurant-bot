package com.restaurante.pedidos.application.usecases;

import com.restaurante.pedidos.application.ports.in.IConfirmarPedidoPort;
import com.restaurante.pedidos.application.ports.out.IPedidoRepository;
import com.restaurante.pedidos.domain.model.Pedido;

public class ConfirmarPedidoUseCase implements IConfirmarPedidoPort {

    private final IPedidoRepository pedidoRepository;

    public ConfirmarPedidoUseCase(IPedidoRepository pedidoRepository) {
        this.pedidoRepository = pedidoRepository;
    }

    @Override
    public Pedido ejecutar(String pedidoId) {
        Pedido pedido = pedidoRepository.buscarPorId(pedidoId)
                .orElseThrow(() -> new IllegalArgumentException("El pedido con ID " + pedidoId + " no existe."));

        // Ejecuta la regla del Dominio: muta el estado a ESPERANDO_PAGO
        pedido.confirmarPorNegocio();

        pedidoRepository.guardar(pedido);
        return pedido;
    }
}