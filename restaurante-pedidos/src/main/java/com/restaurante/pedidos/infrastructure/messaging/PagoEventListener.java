package com.restaurante.pedidos.infrastructure.messaging;

import com.restaurante.pedidos.application.ports.in.IRecibirComprobantePagoPort;
import com.restaurante.shared.event.PagoCompletadoEvent;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

@Component
public class PagoEventListener {

    private final IRecibirComprobantePagoPort recibirComprobantePagoPort;

    public PagoEventListener(IRecibirComprobantePagoPort recibirComprobantePagoPort) {
        this.recibirComprobantePagoPort = recibirComprobantePagoPort;
    }

    @EventListener
    public void manejarPagoCompletado(PagoCompletadoEvent event) {
        if ("APROBADO".equals(event.getStatus())) {
            // El evento acciona de forma automática la transición del pedido hacia PENDIENTE_VALIDACION_PAGO
            recibirComprobantePagoPort.ejecutar(event.getPedidoId());
        }
    }
}