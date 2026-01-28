package com.gestion.erp.modules.auth.dtos;

import com.gestion.erp.modules.auth.models.enums.RolUsuario;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record UsuarioResponseDTO(
    @NotNull Long id,
    @Email String mail,
    @NotBlank String password,
    @NotBlank String nombre,
    @NotBlank String apellido,
    @NotNull RolUsuario rol,
    @NotNull Boolean activo
) {
}
