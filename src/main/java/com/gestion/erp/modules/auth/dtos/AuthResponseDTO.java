package com.gestion.erp.modules.auth.dtos;

public record AuthResponseDTO(
    String token,
    String email,
    String rol
) {}
