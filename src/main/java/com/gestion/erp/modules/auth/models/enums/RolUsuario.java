package com.gestion.erp.modules.auth.models.enums;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum RolUsuario {

    ADMIN("Administrador del sistema"),
    SUPERVISOR("Supervisor de operaciones"),
    ADMINISTRATIVO("Personal administrativo"),
    SUPERVISOR_PLANTA("Supervisor de planta"),
    TOTAL("Acceso total al sistema");
    
    private final String descripcion;
}
