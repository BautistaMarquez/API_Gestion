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
    String nombreEquipo, // "Flattening": Aplanamos la relación para el frontend
    Long equipoId, // ID del equipo para validaciones
    Long supervisorId, // ID del supervisor del equipo
    String supervisorNombre // Nombre completo del supervisor
) {}