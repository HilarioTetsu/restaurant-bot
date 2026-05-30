package com.restaurante.pagos.domain.model;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public class TransaccionPago {
    private Long id;
    private final String pedidoId;
    private final BigDecimal monto;
    private EstadoPago estado;
    private final LocalDateTime fechaProcesamiento;
    private String detallesValidacion; // Almacenará logs del OCR o Banxico

    public TransaccionPago(String pedidoId, BigDecimal monto) {
        if (monto.compareTo(BigDecimal.ZERO) <= 0) {
            throw new IllegalArgumentException("El monto a pagar debe ser mayor a cero.");
        }
        this.pedidoId = pedidoId;
        this.monto = monto;
        this.estado = EstadoPago.PROCESANDO;
        this.fechaProcesamiento = LocalDateTime.now();
    }

    public void registrarAprobacion(String comprobanteId) {
        this.estado = EstadoPago.APROBADO;
        this.detallesValidacion = "Validado exitosamente. ID Rastreo: " + comprobanteId;
    }

    public void registrarRechazo(String motivo) {
        this.estado = EstadoPago.RECHAZADO;
        this.detallesValidacion = "Rechazado debido a: " + motivo;
    }

    // Getters puros
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public String getPedidoId() { return pedidoId; }
    public BigDecimal getMonto() { return monto; }
    public EstadoPago getEstado() { return estado; }
    public LocalDateTime getFechaProcesamiento() { return fechaProcesamiento; }
    public String getDetallesValidacion() { return detallesValidacion; }
}