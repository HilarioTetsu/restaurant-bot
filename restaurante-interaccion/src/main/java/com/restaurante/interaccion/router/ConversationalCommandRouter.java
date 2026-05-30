package com.restaurante.interaccion.router;

import com.restaurante.pedidos.application.ports.in.IAgregarPlatilloPort;
import com.restaurante.pedidos.application.ports.in.ICancelarPedidoPort;
import com.restaurante.pedidos.application.ports.in.ISolicitarConfirmacionPort;
import com.restaurante.pedidos.domain.model.Pedido;
import org.springframework.stereotype.Component;

@Component
public class ConversationalCommandRouter {

    private final IAgregarPlatilloPort agregarPlatilloPort;
    private final ISolicitarConfirmacionPort solicitarConfirmacionPort;
    private final ICancelarPedidoPort cancelarPedidoPort;

    public ConversationalCommandRouter(IAgregarPlatilloPort agregarPlatilloPort,
                                       ISolicitarConfirmacionPort solicitarConfirmacionPort,
                                       ICancelarPedidoPort cancelarPedidoPort) {
        this.agregarPlatilloPort = agregarPlatilloPort;
        this.solicitarConfirmacionPort = solicitarConfirmacionPort;
        this.cancelarPedidoPort = cancelarPedidoPort;
    }

    /**
     * Procesa la cadena de texto recibida de WhatsApp, ejecuta el caso de uso
     * idóneo y retorna el mensaje formateado que el bot enviará de vuelta al usuario.
     */
    public String procesarComando(String textoMensaje, String telefonoCliente) {
        String comandoClean = textoMensaje.trim().toUpperCase();
        String pedidoId = "session-" + telefonoCliente;

        try {
            // Opción A: El usuario envía un ID numérico -> Agregar comida al carrito
            if (comandoClean.matches("\\d+")) {
                Long platilloId = Long.parseLong(comandoClean);
                Pedido pedido = agregarPlatilloPort.ejecutar(pedidoId, telefonoCliente, platilloId, 1);

                return "🛒 *¡Platillo añadido con éxito!*\n\n" +
                        "El total acumulado en tu carrito es de: *$" + pedido.getTotal() + "*\n\n" +
                        "• Envía otro *ID de platillo* si deseas seguir sumando comida.\n" +
                        "• Escribe *CONFIRMAR* si estás listo para enviar la orden a la cocina.\n" +
                        "• Escribe *CANCELAR* si deseas borrar tu carrito de hoy.";
            }

            // Opción B: El usuario decide cerrar su carrito y enviarlo a revisión
            if ("CONFIRMAR".equals(comandoClean)) {
                Pedido pedido = solicitarConfirmacionPort.ejecutar(pedidoId);
                return "🚀 *¡Tu pedido ha sido enviado al restaurante!*\n\n" +
                        "El personal está verificando la disponibilidad en cocina. " +
                        "En breve te notificaremos cuando la orden sea aceptada para que procedas con tu pago.\n" +
                        "Estatus de tu orden: *" + pedido.getEstado() + "*";
            }

            // Opción C: El usuario decide cancelar el pedido en creación
            if ("CANCELAR".equals(comandoClean)) {
                Pedido pedido = cancelarPedidoPort.ejecutar(pedidoId, "Cancelado por el usuario desde WhatsApp.");
                return "❌ *Tu pedido ha sido cancelado exitosamente.*\n\n" +
                        "Si deseas ordenar algo más en el futuro, vuelve a enviarme el ID de cualquier platillo de nuestro menú.";
            }

            // Opción por defecto: Mensaje de ayuda / Bienvenida estándar
            return "👋 *¡Hola! Bienvenido a nuestro sistema de pedidos automatizado.*\n\n" +
                    "Ordenar es muy sencillo, solo utiliza las siguientes palabras clave:\n" +
                    "👉 Envía el *ID numérico* de cualquier alimento para sumarlo a tu carrito (ej. *1*, *2*).\n" +
                    "👉 Escribe *CONFIRMAR* para enviar tu carrito en revisión al restaurante.\n" +
                    "👉 Escribe *CANCELAR* si deseas descartar por completo tu sesión de compra.";

        } catch (IllegalStateException | IllegalArgumentException ex) {
            // Atrapamos errores lógicos del dominio (ej. Carrito vacío, pedido cerrado) y los bajamos al usuario
            return "⚠️ *Nota:* " + ex.getMessage();
        } catch (Exception e) {
            return "💥 Lo sentimos, ocurrió una interrupción técnica en nuestro sistema. Por favor, intenta de nuevo en unos momentos.";
        }
    }
}