package com.gestion.erp.modules.maestros.models.enums;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum EstadoVehiculo {
    
    DISPONIBLE("El vehículo está listo para ser asignado a un viaje"),
    EN_VIAJE("El vehículo se encuentra actualmente en ruta"),
    MANTENIMIENTO("El vehículo está en taller y no puede ser utilizado"),
    ELIMINADO("El vehículo ha sido eliminado del sistema");

    private final String descripcion;
}
