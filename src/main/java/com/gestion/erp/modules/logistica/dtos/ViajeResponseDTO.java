package com.gestion.erp.modules.logistica.dtos;

import com.gestion.erp.modules.logistica.models.enums.EstadoViaje;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

public record ViajeResponseDTO(
    Long id,
    LocalDateTime fechaInicio,
    LocalDateTime fechaFin,
    EstadoViaje estado,
    BigDecimal ventaTotal,
    String vehiculoPatente,      // Simplificamos para el frontend
    String conductorNombre,      // "Nombre Apellido"
    String supervisorNombre,     // Quien autorizó
    List<DetalleResponseDTO> detalles
) {}