package com.gestion.erp.modules.maestros.dtos;

import jakarta.validation.constraints.NotBlank;

public record VehiculoRequestDTO(
    @NotBlank String patente,
    @NotBlank String modelo
) {}
