package com.gestion.erp.modules.maestros.dtos;

import jakarta.validation.constraints.NotBlank;

/**
 * DTO para la creación y actualización de productos.
 * Contiene las validaciones necesarias para asegurar la integridad de entrada.
 */
public record ProductoRequestDTO(
    @NotBlank(message = "El nombre es obligatorio")
    String nombre
) {}