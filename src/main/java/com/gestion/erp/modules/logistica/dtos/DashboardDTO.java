package com.gestion.erp.modules.logistica.dtos;

import java.math.BigDecimal;

public record DashboardDTO(
    Long totalViajes,
    Long viajesEnProceso,
    Long viajesFinalizados,
    BigDecimal ventaTotalEfectiva,
    Double promedioVentaPorViaje
) {}