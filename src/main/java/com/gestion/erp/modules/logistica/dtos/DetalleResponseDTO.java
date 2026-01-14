package com.gestion.erp.modules.logistica.dtos;

import java.math.BigDecimal;

public record DetalleResponseDTO(
    Long id,
    Long productoId,
    String productoNombre,
    Integer cantidadInicial,
    Integer cantidadFinal,
    BigDecimal precioAplicado,
    BigDecimal ventaRealizada
) {}