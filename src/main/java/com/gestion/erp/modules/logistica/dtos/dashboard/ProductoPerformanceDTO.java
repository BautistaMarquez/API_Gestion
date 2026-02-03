package com.gestion.erp.modules.logistica.dtos.dashboard;

import java.math.BigDecimal;

public record ProductoPerformanceDTO(
    String productoNombre,
    Long cantidadVendida,
    BigDecimal totalFacturado
) {}
