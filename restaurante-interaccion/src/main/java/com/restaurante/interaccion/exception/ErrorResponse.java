package com.restaurante.interaccion.exception;

import java.time.LocalDateTime;

public class ErrorResponse {
    private final String error;
    private final String mensaje;
    private final int estatus;
    private final LocalDateTime timestamp;

    public ErrorResponse(String error, String mensaje, int estatus) {
        this.error = error;
        this.mensaje = mensaje;
        this.estatus = estatus;
        this.timestamp = LocalDateTime.now();
    }

    // Getters para la serialización de Jackson
    public String getError() { return error; }
    public String getMensaje() { return mensaje; }
    public int getEstatus() { return estatus; }
    public LocalDateTime getTimestamp() { return timestamp; }
}