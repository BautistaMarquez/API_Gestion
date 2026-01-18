package com.gestion.erp.modules.auth.dtos;

import jakarta.validation.constraints.NotBlank;


// Para recibir las credenciales
public record LoginRequestDTO(
    @NotBlank String email,
    @NotBlank String password
) {}

