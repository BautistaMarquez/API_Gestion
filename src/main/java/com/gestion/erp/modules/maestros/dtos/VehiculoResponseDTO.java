package com.gestion.erp.modules.maestros.dtos;

import com.gestion.erp.modules.maestros.models.enums.EstadoVehiculo;

public record VehiculoResponseDTO(
    Long id,
    String patente,
    String modelo,
    EstadoVehiculo estado
) {}