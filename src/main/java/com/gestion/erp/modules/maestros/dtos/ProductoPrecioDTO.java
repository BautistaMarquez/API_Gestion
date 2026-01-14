package com.gestion.erp.modules.maestros.dtos;
import java.math.BigDecimal;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;

public record ProductoPrecioDTO(
    Long id,
    @NotBlank String etiqueta,
    @Min(0) BigDecimal valor
) {}