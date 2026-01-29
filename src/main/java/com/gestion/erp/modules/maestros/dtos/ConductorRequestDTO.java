package com.gestion.erp.modules.maestros.dtos;

import jakarta.validation.constraints.NotBlank;
import java.time.LocalDate;
import jakarta.validation.constraints.NotNull;

public record ConductorRequestDTO(
    @NotBlank String nombre,
    @NotBlank String apellido,
    @NotBlank String dni,
    @NotNull LocalDate licenciaVencimiento,
    Long equipoId
) {}