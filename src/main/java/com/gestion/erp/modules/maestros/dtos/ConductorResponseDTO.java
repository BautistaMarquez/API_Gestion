package com.gestion.erp.modules.maestros.dtos;

import com.gestion.erp.modules.maestros.models.enums.EstadoConductor;
import java.time.LocalDate;

public record ConductorResponseDTO(
    Long id,
    String nombre,
    String apellido,
    String dni,
    EstadoConductor estado,
    LocalDate licenciaVencimiento,
    String nombreEquipo // "Flattening": Aplanamos la relación para el frontend
) {}