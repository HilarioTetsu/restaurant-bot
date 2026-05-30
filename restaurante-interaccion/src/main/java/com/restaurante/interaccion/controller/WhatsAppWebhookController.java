package com.restaurante.interaccion.controller;

import com.restaurante.pedidos.application.ports.in.IAgregarPlatilloPort;
import com.restaurante.pedidos.domain.model.Pedido;
import com.restaurante.shared.tenant.TenantContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/v1/whatsapp")
public class WhatsAppWebhookController {

    private static final Logger log = LoggerFactory.getLogger(WhatsAppWebhookController.class);
    private static final String VERIFY_TOKEN = "mi_token_secreto_restaurante_bot";

    private final IAgregarPlatilloPort agregarPlatilloPort;

    // Inyectamos el puerto de entrada del core de pedidos
    public WhatsAppWebhookController(IAgregarPlatilloPort agregarPlatilloPort) {
        this.agregarPlatilloPort = agregarPlatilloPort;
    }

    @GetMapping("/webhook")
    public ResponseEntity<String> verificarWebhook(
            @RequestParam(value = "hub.mode", required = false) String mode,
            @RequestParam(value = "hub.verify_token", required = false) String token,
            @RequestParam(value = "hub.challenge", required = false) String challenge) {

        log.info("Recibida petición de verificación de Meta Webhook...");
        if (mode != null && token != null && "subscribe".equals(mode) && VERIFY_TOKEN.equals(token)) {
            return ResponseEntity.ok(challenge);
        }
        return new ResponseEntity("Fallo de verificación", HttpStatus.FORBIDDEN);
    }

    /**
     * Endpoint receptor de mensajes de WhatsApp (POST).
     * Meta envía las interacciones de todos los restaurantes aquí de forma unificada.
     */
    @PostMapping("/webhook")
    @SuppressWarnings("unchecked")
    public ResponseEntity<Void> recibirMensajeWhatsApp(@RequestBody Map<String, Object> payload) {
        try {
            log.info("Payload recibido desde WhatsApp Cloud API.");

            // 1. Navegación segura dentro del árbol JSON de Meta
            List<Map<String, Object>> entries = (List<Map<String, Object>>) payload.get("entry");
            if (entries == null || entries.isEmpty()) return ResponseEntity.ok().build();

            List<Map<String, Object>> changes = (List<Map<String, Object>>) entries.get(0).get("changes");
            if (changes == null || changes.isEmpty()) return ResponseEntity.ok().build();

            Map<String, Object> value = (Map<String, Object>) changes.get(0).get("value");
            if (value == null) return ResponseEntity.ok().build();

            // 2. Extraer metadatos del canal de destino (¿A qué restaurante le escriben?)
            Map<String, Object> metadata = (Map<String, Object>) value.get("metadata");
            if (metadata == null) return ResponseEntity.ok().build();
            String numeroRemitenteNegocio = (String) metadata.get("display_phone_number");

            // 3. Extraer metadatos del cliente (¿Quién escribe y qué dice?)
            List<Map<String, Object>> messages = (List<Map<String, Object>>) value.get("messages");
            if (messages == null || messages.isEmpty()) return ResponseEntity.ok().build();

            Map<String, Object> mensajeRaiz = messages.get(0);
            String telefonoCliente = (String) mensajeRaiz.get("from");

            // Extraer el texto enviado (asumiendo formato text estándar por ahora)
            Map<String, Object> textMap = (Map<String, Object>) mensajeRaiz.get("text");
            String textoMensaje = textMap != null ? (String) textMap.get("body") : "";

            log.info("Mensaje procedente de: {} hacia el negocio: {}. Contenido: '{}'",
                    telefonoCliente, numeroRemitenteNegocio, textoMensaje);

            // 4. RESOLUCIÓN DINÁMICA DEL TENANT BBDD
            // En producción, esto consultaría el esquema 'public' para buscar qué restaurante posee este número.
            // Para desarrollo local, simulamos el diccionario de enrutamiento:
            String tenantSchema = resolverEsquemaPorNumero(numeroRemitenteNegocio);

            // Establecemos de forma manual el esquema en el hilo de ejecución actual
            TenantContext.setCurrentTenant(tenantSchema);
            log.info("Conmutando search_path de PostgreSQL al esquema: '{}'", tenantSchema);

            // 5. ACCIONAR EL CORE DE NUESTRA ARQUITECTURA HEXAGONAL
            // Simulamos que si el usuario manda un número del 1 al 4, quiere agregar ese platillo al carrito.
            // Usamos el teléfono del cliente como el ID único de la sesión conversacional.
            if (textoMensaje.matches("\\d+")) {
                Long platilloId = Long.parseLong(textoMensaje);

                Pedido pedidoActualizado = agregarPlatilloPort.ejecutar(
                        "session-" + telefonoCliente, // ID de sesión conversacional único
                        telefonoCliente,
                        platilloId,
                        1 // Añade una unidad por interacción estándar
                );

                log.info("Pedido '{}' actualizado con éxito. Total actual: ${}",
                        pedidoActualizado.getId(), pedidoActualizado.getTotal());
            }

            // Respondemos siempre con un 200 OK a Meta para evitar reintentos del webhook
            return ResponseEntity.ok().build();

        } catch (Exception e) {
            log.error("Error crítico procesando el flujo de WhatsApp Webhook", e);
            return ResponseEntity.ok().build();
        } finally {
            // CRUCIAL: Limpieza del hilo para evitar fugas de memoria
            TenantContext.clear();
        }
    }

    private String resolverEsquemaPorNumero(String numeroTelefono) {
        if ("528340000001".equals(numeroTelefono)) {
            return "tacos_centro";
        } else if ("528340000002".equals(numeroTelefono)) {
            return "las_brisas";
        }
        return TenantContext.DEFAULT_TENANT;
    }
}