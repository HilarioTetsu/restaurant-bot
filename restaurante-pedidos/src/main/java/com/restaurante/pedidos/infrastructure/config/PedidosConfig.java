package com.restaurante.pedidos.infrastructure.config;

import com.restaurante.pedidos.application.ports.in.*;
import com.restaurante.pedidos.application.usecases.*;
import com.restaurante.pedidos.application.ports.out.IPedidoRepository;
import com.restaurante.pedidos.application.ports.out.IPlatilloRepository;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class PedidosConfig {

    @Bean
    public AgregarPlatilloUseCase agregarPlatilloUseCase(
            IPedidoRepository pedidoRepository,
            IPlatilloRepository platilloRepository) {
        return new AgregarPlatilloUseCase(pedidoRepository, platilloRepository);
    }

    @Bean
    public ISolicitarConfirmacionPort solicitarConfirmacionPort(IPedidoRepository pedidoRepository) {
        return new SolicitarConfirmacionUseCase(pedidoRepository);
    }

    @Bean
    public IConfirmarPedidoPort confirmarPedidoPort(IPedidoRepository pedidoRepository) {
        return new ConfirmarPedidoUseCase(pedidoRepository);
    }

    @Bean
    public IRecibirComprobantePagoPort recibirComprobantePagoPort(IPedidoRepository pedidoRepository) {
        return new RecibirComprobantePagoUseCase(pedidoRepository);
    }

    @Bean
    public IAprobarPagoPort aprobarPagoPort(IPedidoRepository pedidoRepository) {
        return new AprobarPagoUseCase(pedidoRepository);
    }

    @Bean
    public IMarcarPedidoListoPort marcarPedidoListoPort(IPedidoRepository pedidoRepository) {
        return new MarcarPedidoListoUseCase(pedidoRepository);
    }

    @Bean
    public ICancelarPedidoPort cancelarPedidoPort(IPedidoRepository pedidoRepository) {
        return new CancelarPedidoUseCase(pedidoRepository);
    }
}