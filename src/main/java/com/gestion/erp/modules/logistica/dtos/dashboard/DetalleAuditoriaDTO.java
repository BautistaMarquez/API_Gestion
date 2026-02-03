package com.gestion.erp.modules.logistica.dtos.dashboard;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public record DetalleAuditoriaDTO(
    LocalDateTime fechaCierre,
    String nombreConductor,
    String vehiculoPatente,
    String nombreEquipo,
    String nombreProducto,
    Integer cargaInicial,
    Integer cargaFinal,
    Integer unidadesVendidas,
    BigDecimal precioUnitarioSnapshot,
    BigDecimal subtotalVenta
) {}
