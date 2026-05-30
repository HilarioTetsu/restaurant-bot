package com.restaurante.interaccion.controller;

import com.restaurante.pedidos.application.ports.in.*;
import com.restaurante.pedidos.domain.model.Pedido;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/v1/pedidos")
public class PedidoController {

    private final IAgregarPlatilloPort agregarPlatilloPort;
    private final ISolicitarConfirmacionPort solicitarConfirmacionPort;
    private final IConfirmarPedidoPort confirmarPedidoPort;
    private final IRecibirComprobantePagoPort recibirComprobantePagoPort;
    private final IAprobarPagoPort aprobarPagoPort;
    private final IMarcarPedidoListoPort marcarPedidoListoPort;
    private final ICancelarPedidoPort cancelarPedidoPort;

    public PedidoController(IAgregarPlatilloPort agregarPlatilloPort,
                            ISolicitarConfirmacionPort solicitarConfirmacionPort,
                            IConfirmarPedidoPort confirmarPedidoPort,
                            IRecibirComprobantePagoPort recibirComprobantePagoPort,
                            IAprobarPagoPort aprobarPagoPort,
                            IMarcarPedidoListoPort marcarPedidoListoPort,
                            ICancelarPedidoPort cancelarPedidoPort) {
        this.agregarPlatilloPort = agregarPlatilloPort;
        this.solicitarConfirmacionPort = solicitarConfirmacionPort;
        this.confirmarPedidoPort = confirmarPedidoPort;
        this.recibirComprobantePagoPort = recibirComprobantePagoPort;
        this.aprobarPagoPort = aprobarPagoPort;
        this.marcarPedidoListoPort = marcarPedidoListoPort;
        this.cancelarPedidoPort = cancelarPedidoPort;
    }

    @PostMapping("/items")
    public ResponseEntity<Pedido> agregarPlatillo(@RequestBody Map<String, Object> payload) {
        String pedidoId = (String) payload.get("pedido_id");
        String telefonoCliente = (String) payload.get("telefono_cliente");
        Long platilloId = ((Number) payload.get("platillo_id")).longValue();
        int cantidad = payload.containsKey("cantidad") ? ((Number) payload.get("cantidad")).intValue() : 1;

        Pedido pedido = agregarPlatilloPort.ejecutar(pedidoId, telefonoCliente, platilloId, cantidad);
        return ResponseEntity.ok(pedido);
    }

    @PutMapping("/{id}/solicitar-confirmacion")
    public ResponseEntity<Pedido> solicitarConfirmacion(@PathVariable("id") String pedidoId) {
        Pedido pedido = solicitarConfirmacionPort.ejecutar(pedidoId);
        return ResponseEntity.ok(pedido);
    }

    @PutMapping("/{id}/confirmar")
    public ResponseEntity<Pedido> confirmarPedido(@PathVariable("id") String pedidoId) {
        Pedido pedido = confirmarPedidoPort.ejecutar(pedidoId);
        return ResponseEntity.ok(pedido);
    }

    // Transición 3: El bot intercepta la foto de la transferencia enviada por el cliente
    @PutMapping("/{id}/comprobante")
    public ResponseEntity<Pedido> recibirComprobante(@PathVariable("id") String pedidoId) {
        Pedido pedido = recibirComprobantePagoPort.ejecutar(pedidoId);
        return ResponseEntity.ok(pedido);
    }

    // Transición 4: El staff o el sistema asíncrono aprueban el depósito bancario
    @PutMapping("/{id}/aprobar-pago")
    public ResponseEntity<Pedido> aprobarPago(@PathVariable("id") String pedidoId) {
        Pedido pedido = aprobarPagoPort.ejecutar(pedidoId);
        return ResponseEntity.ok(pedido);
    }

    // Transición 5: El área de cocina marca los platillos como listos para entrega
    @PutMapping("/{id}/listo")
    public ResponseEntity<Pedido> marcarListo(@PathVariable("id") String pedidoId) {
        Pedido pedido = marcarPedidoListoPort.ejecutar(pedidoId);
        return ResponseEntity.ok(pedido);
    }

    // Flujo Alterno: Cancelación lógica del pedido por cualquier actor calificado
    @PutMapping("/{id}/cancelar")
    public ResponseEntity<Pedido> cancelar(@PathVariable("id") String pedidoId, @RequestBody Map<String, String> payload) {
        String motivo = payload.getOrDefault("motivo", "No especificado");
        Pedido pedido = cancelarPedidoPort.ejecutar(pedidoId, motivo);
        return ResponseEntity.ok(pedido);
    }
}