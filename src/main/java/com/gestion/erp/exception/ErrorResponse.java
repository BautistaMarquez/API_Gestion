package com.gestion.erp.exception;

import com.fasterxml.jackson.annotation.JsonInclude;
import java.time.LocalDateTime;
import java.util.List;

@JsonInclude(JsonInclude.Include.NON_NULL) // No envía los campos nulos al cliente
public record ErrorResponse(
    String codigo,
    String mensaje,
    LocalDateTime timestamp,
    List<String> detalles // Para errores de validación de campos
) {
    public ErrorResponse(String codigo, String mensaje) {
        this(codigo, mensaje, LocalDateTime.now(), null);
    }

    public ErrorResponse(String codigo, String mensaje, List<String> detalles) {
        this(codigo, mensaje, LocalDateTime.now(), detalles);
    }
}
