package com.restaurante.pagos.infrastructure.config;

import com.restaurante.pagos.application.ports.in.IProcesarComprobantePort;
import com.restaurante.pagos.application.ports.out.IEventPublisherPort;
import com.restaurante.pagos.application.ports.out.IPagoRepository;
import com.restaurante.pagos.application.usecases.ProcesarComprobanteUseCase;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class PagosConfig {

    @Bean
    public IProcesarComprobantePort procesarComprobantePort(
            IPagoRepository pagoRepository,
            IEventPublisherPort eventPublisherPort) {
        return new ProcesarComprobanteUseCase(pagoRepository, eventPublisherPort);
    }
}