package com.gestion.erp.modules.logistica.dtos;

public record DashboardDTO(
    long totalViajes,
    long viajesEnProceso,
    long viajesFinalizados,
    double ventaTotalEfectiva,
    double promedioVentaPorViaje
) {}