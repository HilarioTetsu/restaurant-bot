package com.restaurante.interaccion.controller;

import com.restaurante.pedidos.application.ports.in.IAgregarPlatilloPort;
import com.restaurante.pedidos.domain.model.Pedido;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/v1/pedidos")
public class PedidoController {

    private final IAgregarPlatilloPort agregarPlatilloPort;

   
    public PedidoController(IAgregarPlatilloPort agregarPlatilloPort) {
        this.agregarPlatilloPort = agregarPlatilloPort;
    }

    @PostMapping("/items")
    public ResponseEntity<Pedido> agregarPlatillo(@RequestBody Map<String, Object> payload) {
        
        String pedidoId = (String) payload.get("pedido_id");
        String telefonoCliente = (String) payload.get("telefono_cliente");
        
       
        Long platilloId = ((Number) payload.get("platillo_id")).longValue();

        
        Pedido pedido = agregarPlatilloPort.ejecutar(pedidoId, telefonoCliente, platilloId);
        
        return ResponseEntity.ok(pedido);
    }
}