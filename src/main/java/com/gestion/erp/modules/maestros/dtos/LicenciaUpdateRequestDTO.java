package com.gestion.erp.modules.maestros.dtos;

import java.time.LocalDate;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class LicenciaUpdateRequestDTO {
    @NotNull(message = "La nueva fecha de vencimiento es obligatoria")
    private LocalDate nuevaFechaVencimiento;
}
