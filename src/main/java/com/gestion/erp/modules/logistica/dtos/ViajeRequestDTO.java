package com.gestion.erp.modules.logistica.dtos;

import java.util.List;

import jakarta.validation.constraints.NotNull;

public record ViajeRequestDTO(

    @NotNull(message = "El ID del vehículo es obligatorio")
    Long vehiculoId,
    @NotNull(message = "El ID del conductor es obligatorio")
    Long conductorId,
    @NotNull(message = "El ID del supervisor es obligatorio")
    Long supervisorId,
    @NotNull(message = "No se puede crear un viaje sin productos")
    List<DetalleRequestDTO> detalles
) {}
