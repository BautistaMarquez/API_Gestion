package com.gestion.erp.modules.maestros.dtos;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class EstadoUpdateRequestDTO<T> {
    @NotNull(message = "El nuevo estado es obligatorio")
    private T nuevoEstado;
}
