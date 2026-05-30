package com.restaurante.pedidos.application.usecases;

import com.restaurante.pedidos.application.ports.in.IRecibirComprobantePagoPort;
import com.restaurante.pedidos.application.ports.out.IPedidoRepository;
import com.restaurante.pedidos.domain.model.Pedido;

public class RecibirComprobantePagoUseCase implements IRecibirComprobantePagoPort {

    private final IPedidoRepository pedidoRepository;

    public RecibirComprobantePagoUseCase(IPedidoRepository pedidoRepository) {
        this.pedidoRepository = pedidoRepository;
    }

    @Override
    public Pedido ejecutar(String pedidoId) {
        Pedido pedido = pedidoRepository.buscarPorId(pedidoId)
                .orElseThrow(() -> new IllegalArgumentException("El pedido con ID " + pedidoId + " no existe."));

        pedido.recibirComprobantePago();
        pedidoRepository.guardar(pedido);
        return pedido;
    }
}