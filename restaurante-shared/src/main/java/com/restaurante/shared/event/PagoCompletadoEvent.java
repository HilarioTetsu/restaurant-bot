package com.restaurante.shared.event;

import java.math.BigDecimal;

public class PagoCompletadoEvent {
    private final String pedidoId;
    private final BigDecimal monto;
    private final String status; // "APROBADO" o "RECHAZADO"

    public PagoCompletadoEvent(String pedidoId, BigDecimal monto, String status) {
        this.pedidoId = pedidoId;
        this.monto = monto;
        this.status = status;
    }

    public String getPedidoId() { return pedidoId; }
    public BigDecimal getMonto() { return monto; }
    public String getStatus() { return status; }
}