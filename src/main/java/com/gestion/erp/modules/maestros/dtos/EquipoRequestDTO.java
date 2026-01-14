package com.gestion.erp.modules.maestros.dtos;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record EquipoRequestDTO(
    @NotBlank(message = "El nombre del equipo es obligatorio") 
    String nombre,
    
    @NotNull(message = "Debe asignar un supervisor al equipo") 
    Long supervisorId
) {}
