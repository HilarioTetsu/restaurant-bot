package com.restaurante.pagos.application.ports.in;

import com.restaurante.pagos.domain.model.TransaccionPago;
import java.math.BigDecimal;

public interface IProcesarComprobantePort {
    TransaccionPago ejecutar(String pedidoId, BigDecimal montoDeclarado);
}