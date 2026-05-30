package com.restaurante.interaccion.controller;

import com.restaurante.pagos.application.ports.in.IProcesarComprobantePort;
import com.restaurante.pagos.domain.model.TransaccionPago;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.Map;

@RestController
@RequestMapping("/v1/pagos")
public class PagoController {

    private final IProcesarComprobantePort procesarComprobantePort;

    public PagoController(IProcesarComprobantePort procesarComprobantePort) {
        this.procesarComprobantePort = procesarComprobantePort;
    }

    @PostMapping("/comprobantes")
    public ResponseEntity<TransaccionPago> simularEnvioComprobante(@RequestBody Map<String, Object> payload) {
        String pedidoId = (String) payload.get("pedido_id");
        BigDecimal monto = new BigDecimal(payload.get("monto").toString());

        TransaccionPago transaccion = procesarComprobantePort.ejecutar(pedidoId, monto);
        return ResponseEntity.ok(transaccion);
    }
}