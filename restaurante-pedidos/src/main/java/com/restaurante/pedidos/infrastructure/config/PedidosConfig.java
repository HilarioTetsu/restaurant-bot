package com.restaurante.pedidos.infrastructure.config;

import com.restaurante.pedidos.application.ports.in.IConfirmarPedidoPort;
import com.restaurante.pedidos.application.ports.in.ISolicitarConfirmacionPort;
import com.restaurante.pedidos.application.usecases.ConfirmarPedidoUseCase;
import com.restaurante.pedidos.application.usecases.SolicitarConfirmacionUseCase;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.restaurante.pedidos.application.ports.out.IPedidoRepository;
import com.restaurante.pedidos.application.ports.out.IPlatilloRepository;
import com.restaurante.pedidos.application.usecases.AgregarPlatilloUseCase;

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
}
