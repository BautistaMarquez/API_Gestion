package com.gestion.erp.modules.maestros.dtos;

import java.util.List;
/**
 * DTO para el envío de información de productos hacia el exterior.
 * No requiere anotaciones de validación ya que representa datos ya procesados.
 */
public record ProductoResponseDTO(
    Long id,
    String nombre,
    Boolean activo,
    List<ProductoPrecioDTO> precios
) {}
