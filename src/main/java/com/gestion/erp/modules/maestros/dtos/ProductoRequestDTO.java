package com.gestion.erp.modules.maestros.dtos;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import java.math.BigDecimal;

/**
 * DTO para la creación y actualización de productos.
 * Contiene las validaciones necesarias para asegurar la integridad de entrada.
 */
public record ProductoRequestDTO(
    @NotBlank(message = "El nombre es obligatorio")
    String nombre,
    
    @NotNull(message = "El precio es obligatorio")
    @DecimalMin(value = "0.01", message = "El precio debe ser mayor a cero")
    BigDecimal precioUnitario
) {}