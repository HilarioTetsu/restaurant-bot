package com.restaurante.interaccion.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class GlobalExceptionHandler {

    // Captura violaciones en la máquina de estados o flujos ilegales del negocio
    @ExceptionHandler(IllegalStateException.class)
    public ResponseEntity<ErrorResponse> manejarIllegalStateException(IllegalStateException ex) {
        ErrorResponse error = new ErrorResponse(
                "ACCION_INVALIDA",
                ex.getMessage(),
                HttpStatus.BAD_REQUEST.value()
        );
        return new ResponseEntity<>(error, HttpStatus.BAD_REQUEST);
    }

    // Captura validaciones de argumentos incorrectos (ej. IDs inexistentes o productos no disponibles)
    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<ErrorResponse> manejarIllegalArgumentException(IllegalArgumentException ex) {
        ErrorResponse error = new ErrorResponse(
                "DATOS_ERRONEOS",
                ex.getMessage(),
                HttpStatus.BAD_REQUEST.value()
        );
        return new ResponseEntity<>(error, HttpStatus.BAD_REQUEST);
    }

    // Captura cualquier otro error inesperado de infraestructura para no exponer el código interno
    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorResponse> manejarExcepcionesGenerales(Exception ex) {
        ErrorResponse error = new ErrorResponse(
                "ERROR_INTERNO",
                "Ocurrió un error inesperado en el servidor. Por favor, intente más tarde.",
                HttpStatus.INTERNAL_SERVER_ERROR.value()
        );
        return new ResponseEntity<>(error, HttpStatus.INTERNAL_SERVER_ERROR);
    }
}