package com.restaurante.pagos.application.usecases;

import com.restaurante.pagos.application.ports.in.IProcesarComprobantePort;
import com.restaurante.pagos.application.ports.out.IPagoRepository;
import com.restaurante.pagos.domain.model.TransaccionPago;
import java.math.BigDecimal;

public class ProcesarComprobanteUseCase implements IProcesarComprobantePort {

    private final IPagoRepository pagoRepository;

    public ProcesarComprobanteUseCase(IPagoRepository pagoRepository) {
        this.pagoRepository = pagoRepository;
    }

    @Override
    public TransaccionPago ejecutar(String pedidoId, BigDecimal montoDeclarado) {
        // 1. Inicializamos la transacción con reglas de dominio puros
        TransaccionPago pago = new TransaccionPago(pedidoId, montoDeclarado);

        // 2. Simulamos la validación inmediata (en producción aquí entraría el motor OCR/Banxico)
        // Por ahora lo aprobamos directamente de forma automática
        pago.registrarAprobacion("SPEI-TRANS-ID-99999");

        // 3. Persistimos el registro financiero
        pagoRepository.guardar(pago);

        return pago;
    }
}