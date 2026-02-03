package com.gestion.erp.modules.logistica.dtos.dashboard;

import java.math.BigDecimal;
import java.time.LocalDate;

public record VentaDiariaDTO(
    LocalDate fecha,
    BigDecimal totalVenta
) {}
