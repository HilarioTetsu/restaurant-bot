package com.restaurante.pedidos.infrastructure.config;

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
	
	
}
