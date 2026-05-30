package com.restaurante.interaccion.controller;

import com.restaurante.pedidos.application.ports.in.IAgregarPlatilloPort;
import com.restaurante.pedidos.application.ports.in.IConfirmarPedidoPort;
import com.restaurante.pedidos.application.ports.in.ISolicitarConfirmacionPort;
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

    public PedidoController(IAgregarPlatilloPort agregarPlatilloPort,
                            ISolicitarConfirmacionPort solicitarConfirmacionPort,
                            IConfirmarPedidoPort confirmarPedidoPort) {
        this.agregarPlatilloPort = agregarPlatilloPort;
        this.solicitarConfirmacionPort = solicitarConfirmacionPort;
        this.confirmarPedidoPort = confirmarPedidoPort;
    }

    @PostMapping("/items")
    public ResponseEntity<Pedido> agregarPlatillo(@RequestBody Map<String, Object> payload) {
        String pedidoId = (String) payload.get("pedido_id");
        String telefonoCliente = (String) payload.get("telefono_cliente");
        Long platilloId = ((Number) payload.get("platillo_id")).longValue();

        // Si no se envía una cantidad explícita en el JSON, por defecto sumará 1
        int cantidad = payload.containsKey("cantidad") ? ((Number) payload.get("cantidad")).intValue() : 1;

        Pedido pedido = agregarPlatilloPort.ejecutar(pedidoId, telefonoCliente, platilloId, cantidad);

        return ResponseEntity.ok(pedido);
    }

    // Transición 1: El cliente envía el carrito desde WhatsApp
    @PutMapping("/{id}/solicitar-confirmacion")
    public ResponseEntity<Pedido> solicitarConfirmacion(@PathVariable("id") String pedidoId) {
        Pedido pedido = solicitarConfirmacionPort.ejecutar(pedidoId);
        return ResponseEntity.ok(pedido);
    }

    // Transición 2: El staff aprueba la disponibilidad desde el Dashboard
    @PutMapping("/{id}/confirmar")
    public ResponseEntity<Pedido> confirmarPedido(@PathVariable("id") String pedidoId) {
        Pedido pedido = confirmarPedidoPort.ejecutar(pedidoId);
        return ResponseEntity.ok(pedido);
    }
}