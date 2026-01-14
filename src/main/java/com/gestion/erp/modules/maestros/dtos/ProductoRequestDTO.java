package com.gestion.erp.modules.maestros.dtos;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import java.util.List;


/**
 * DTO para la creación y actualización de productos.
 * Contiene las validaciones necesarias para asegurar la integridad de entrada.
 */
public record ProductoRequestDTO(
    @NotBlank(message = "El nombre es obligatorio")
    String nombre,

    @NotEmpty(message = "Debe asignar al menos un precio inicial")
    List<PrecioInicialDTO> precios
) {}