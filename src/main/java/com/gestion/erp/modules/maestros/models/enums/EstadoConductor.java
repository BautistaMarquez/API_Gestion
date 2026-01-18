package com.gestion.erp.modules.maestros.models.enums;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum EstadoConductor {

    DISPONIBLE("El conductor está listo para ser asignado a un viaje"),
    OCUPADO("El conductor se encuentra actualmente en ruta"),
    LICENCIA_VENCIDA("El conductor no puede operar debido a la expiración de su licencia"),
    ELIMINADO("El conductor ha sido eliminado del sistema");
    
    private final String descripcion;
}
