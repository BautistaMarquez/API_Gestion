package com.gestion.erp.modules.logistica.dtos;

import java.util.List;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.NotEmpty;

public record ViajeCierreRequestDTO(
    @NotNull(message = "El ID del viaje es obligatorio") 
    Long viajeId,
    
    @NotEmpty(message = "Debe informar el cierre de al menos un producto")
    List<DetalleCierreDTO> detallesFinales
) {}