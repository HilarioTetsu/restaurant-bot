package com.restaurante.pagos.application.usecases;

import com.restaurante.pagos.application.ports.in.IProcesarComprobantePort;
import com.restaurante.pagos.application.ports.out.IEventPublisherPort;
import com.restaurante.pagos.application.ports.out.IPagoRepository;
import com.restaurante.pagos.domain.model.TransaccionPago;
import com.restaurante.shared.event.PagoCompletadoEvent;

import java.math.BigDecimal;

public class ProcesarComprobanteUseCase implements IProcesarComprobantePort {


    private final IPagoRepository pagoRepository;
    private final IEventPublisherPort eventPublisherPort;

    public ProcesarComprobanteUseCase(IPagoRepository pagoRepository, IEventPublisherPort eventPublisherPort) {
        this.pagoRepository = pagoRepository;
        this.eventPublisherPort = eventPublisherPort;
    }


    @Override
    public TransaccionPago ejecutar(String pedidoId, BigDecimal montoDeclarado) {
        TransaccionPago pago = new TransaccionPago(pedidoId, montoDeclarado);

        // Simulación automática de aprobación bancaria/OCR
        pago.registrarAprobacion("SPEI-TRANS-ID-99999");

        pagoRepository.guardar(pago);

        // Publicamos el evento de forma totalmente desacoplada
        eventPublisherPort.publicar(new PagoCompletadoEvent(
                pago.getPedidoId(),
                pago.getMonto(),
                pago.getEstado().name()
        ));

        return pago;
    }
}