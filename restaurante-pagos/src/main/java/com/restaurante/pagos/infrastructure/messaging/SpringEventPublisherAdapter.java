package com.restaurante.pagos.infrastructure.messaging;

import com.restaurante.pagos.application.ports.out.IEventPublisherPort;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Component;

@Component
public class SpringEventPublisherAdapter implements IEventPublisherPort {

    private final ApplicationEventPublisher applicationEventPublisher;

    public SpringEventPublisherAdapter(ApplicationEventPublisher applicationEventPublisher) {
        this.applicationEventPublisher = applicationEventPublisher;
    }

    @Override
    public void publicar(Object evento) {
        applicationEventPublisher.publishEvent(evento);
    }
}