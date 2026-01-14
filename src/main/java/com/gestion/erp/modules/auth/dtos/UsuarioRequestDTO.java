package com.gestion.erp.modules.auth.dtos;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import com.gestion.erp.modules.auth.models.enums.RolUsuario;

public record UsuarioRequestDTO(
    @Email String mail,
    @NotBlank String password,
    @NotBlank String nombre,
    @NotBlank String apellido,
    @NotNull RolUsuario rol
) {}
