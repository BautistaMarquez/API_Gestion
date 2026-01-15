package com.gestion.erp.modules.maestros.dtos;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import java.time.LocalDate;

public record ConductorRequestDTO(
    @NotBlank String nombre,
    @NotBlank String apellido,
    @NotBlank String dni,
    @NotNull LocalDate licenciaVencimiento,
    @NotNull Long equipoId
) {}