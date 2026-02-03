package com.gestion.erp.modules.logistica.dtos.dashboard;

import java.math.BigDecimal;


public record KpiStatsDTO(
    Long totalViajesFinalizados,
    Long viajesEnRuta,         // Estado != FINALIZADO
    BigDecimal ventasTotales,  // Suma de $$$
    Double efectividadCarga    // % de carga vendida
) {}
