package com.gestion.erp.modules.maestros.dtos;
import java.math.BigDecimal;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotBlank;

public record ProductoPrecioDTO(
    Long id,
    @NotBlank String etiqueta,
    @DecimalMin(value = "0.01", message = "El precio debe ser mayor a cero") 
    BigDecimal valor
) {}