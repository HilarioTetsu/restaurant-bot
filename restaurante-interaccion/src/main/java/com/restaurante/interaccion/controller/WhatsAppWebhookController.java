package com.restaurante.interaccion.controller;

import com.restaurante.interaccion.infrastructure.ports.out.IWhatsAppClientPort;
import com.restaurante.interaccion.router.ConversationalCommandRouter;
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

    private final ConversationalCommandRouter commandRouter;
    private final IWhatsAppClientPort whatsAppClientPort;

    // Inyectamos tanto el enrutador como el puerto de salida HTTP
    public WhatsAppWebhookController(ConversationalCommandRouter commandRouter,
                                     IWhatsAppClientPort whatsAppClientPort) {
        this.commandRouter = commandRouter;
        this.whatsAppClientPort = whatsAppClientPort;
    }

    @GetMapping("/webhook")
    public ResponseEntity<String> verificarWebhook(
            @RequestParam(value = "hub.mode", required = false) String mode,
            @RequestParam(value = "hub.verify_token", required = false) String token,
            @RequestParam(value = "hub.challenge", required = false) String challenge) {
        if (mode != null && token != null && "subscribe".equals(mode) && VERIFY_TOKEN.equals(token)) {
            return ResponseEntity.ok(challenge);
        }
        return new ResponseEntity("Fallo de verificación", HttpStatus.FORBIDDEN);
    }

    @PostMapping("/webhook")
    @SuppressWarnings("unchecked")
    public ResponseEntity<Void> recibirMensajeWhatsApp(@RequestBody Map<String, Object> payload) {
        try {
            List<Map<String, Object>> entries = (List<Map<String, Object>>) payload.get("entry");
            if (entries == null || entries.isEmpty()) return ResponseEntity.ok().build();

            List<Map<String, Object>> changes = (List<Map<String, Object>>) entries.get(0).get("changes");
            if (changes == null || changes.isEmpty()) return ResponseEntity.ok().build();

            Map<String, Object> value = (Map<String, Object>) changes.get(0).get("value");
            if (value == null) return ResponseEntity.ok().build();

            Map<String, Object> metadata = (Map<String, Object>) value.get("metadata");
            if (metadata == null) return ResponseEntity.ok().build();
            String numeroRemitenteNegocio = (String) metadata.get("display_phone_number");
            String phoneNumberId = (String) metadata.get("phone_number_id"); // <-- EXTRAEMOS EL ID DE RED DE META

            List<Map<String, Object>> messages = (List<Map<String, Object>>) value.get("messages");
            if (messages == null || messages.isEmpty()) return ResponseEntity.ok().build();

            Map<String, Object> mensajeRaiz = messages.get(0);
            String telefonoCliente = (String) mensajeRaiz.get("from");

            Map<String, Object> textMap = (Map<String, Object>) mensajeRaiz.get("text");
            String textoMensaje = textMap != null ? (String) textMap.get("body") : "";

            // Conmutación dinámica del Tenant
            String tenantSchema = resolverEsquemaPorNumero(numeroRemitenteNegocio);
            TenantContext.setCurrentTenant(tenantSchema);

            log.info("--- NUEVA INTERACCION BOT ---");
            log.info("Inbound SMS -> Cliente: {} | Negocio: '{}' | Texto: '{}'", telefonoCliente, tenantSchema, textoMensaje);

            // Procesar la intención conversacional
            String respuestaBot = commandRouter.procesarComando(textoMensaje, telefonoCliente);

            // DISPARO DEL PUERTO OUTBOUND: Envío del mensaje real al celular del cliente
            whatsAppClientPort.enviarMensajeTexto(phoneNumberId, telefonoCliente, respuestaBot);

            log.info("-----------------------------");
            return ResponseEntity.ok().build();

        } catch (Exception e) {
            log.error("Error procesando flujo en WhatsApp Webhook", e);
            return ResponseEntity.ok().build();
        } finally {
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