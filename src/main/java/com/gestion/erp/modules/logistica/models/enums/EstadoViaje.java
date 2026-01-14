package com.gestion.erp.modules.logistica.models.enums;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum EstadoViaje {

    EN_PROCESO("En Proceso"),
    FINALIZADO("Finalizado");
    
    private final String descripcion;
}
