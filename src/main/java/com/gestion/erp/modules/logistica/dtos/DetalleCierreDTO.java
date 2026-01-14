package com.gestion.erp.modules.logistica.dtos;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PositiveOrZero;

public record DetalleCierreDTO(
    @NotNull(message = "El ID del detalle es obligatorio") 
    Long detalleId,
    
    @NotNull(message = "La cantidad final no puede ser nula")
    @PositiveOrZero(message = "La cantidad final no puede ser negativa")
    Integer cantidadFinal
) {}