package com.restaurante.pedidos.application.usecases;

import com.restaurante.pedidos.application.ports.in.ISolicitarConfirmacionPort;
import com.restaurante.pedidos.application.ports.out.IPedidoRepository;
import com.restaurante.pedidos.domain.model.Pedido;

public class SolicitarConfirmacionUseCase implements ISolicitarConfirmacionPort {

    private final IPedidoRepository pedidoRepository;

    public SolicitarConfirmacionUseCase(IPedidoRepository pedidoRepository) {
        this.pedidoRepository = pedidoRepository;
    }

    @Override
    public Pedido ejecutar(String pedidoId) {
        Pedido pedido = pedidoRepository.buscarPorId(pedidoId)
                .orElseThrow(() -> new IllegalArgumentException("El pedido con ID " + pedidoId + " no existe."));

        // Ejecuta la regla del Dominio: muta el estado a PENDIENTE_CONFIRMACION
        pedido.solicitarConfirmacion();

        pedidoRepository.guardar(pedido);
        return pedido;
    }
}