package com.gestion.erp.modules.maestros.dtos;

public record EquipoResponseDTO(
    Long id,
    String nombre,
    String supervisorNombreCompleto
) {}
