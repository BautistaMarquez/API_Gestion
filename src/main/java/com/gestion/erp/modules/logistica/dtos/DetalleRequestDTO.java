package com.gestion.erp.modules.logistica.dtos;

import jakarta.validation.constraints.NotNull;

public record DetalleRequestDTO(
    @NotNull(message = "El ID del producto es obligatorio")
    Long productoId,
    @NotNull(message = "La cantidad inicial es obligatoria")
    Integer cantidadInicial,
    @NotNull(message = "El ID del precio del producto es obligatorio")
    Long productoPrecioId // Para saber qué tarifa aplicar (Minorista/Mayorista)
) {}