package com.gestion.erp.modules.auth.controllers;

import org.springdoc.core.annotations.ParameterObject;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import jakarta.validation.Valid;

import com.gestion.erp.modules.auth.dtos.ResetPasswordRequest;
import com.gestion.erp.modules.auth.dtos.UsuarioRequestDTO;
import com.gestion.erp.modules.auth.dtos.UsuarioResponseDTO;
import com.gestion.erp.modules.auth.models.Usuario;
import com.gestion.erp.modules.auth.models.enums.RolUsuario;
import com.gestion.erp.modules.auth.services.UsuarioService;
import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import java.util.List;

@RestController
@RequestMapping("/api/v1/usuarios")
@RequiredArgsConstructor
public class UsuarioController {
    private final UsuarioService service;

    @PostMapping
    @PreAuthorize("hasRole('ADMIN') or hasRole('TOTAL')")
    @Operation(summary = "Crear un nuevo usuario")
    public ResponseEntity<Usuario> crear(@Valid @RequestBody UsuarioRequestDTO dto) {
        return new ResponseEntity<>(service.registrar(dto), HttpStatus.CREATED);
    }

    @PatchMapping("/{id}/desactivar")
    @PreAuthorize("hasAnyRole('ADMIN', 'TOTAL')")
    public ResponseEntity<UsuarioResponseDTO> desactivar(@PathVariable Long id) {
        return ResponseEntity.ok(service.eliminarLogico(id));
    }

    @GetMapping
    @PreAuthorize("hasAnyRole('ADMIN', 'TOTAL','ADMINISTRATIVO')")
    @Operation(summary = "Listar usuarios con paginación")
    public ResponseEntity<Page<UsuarioResponseDTO>> listarPaginado(
        @ParameterObject Pageable pageable) { // @ParameterObject ayuda a Swagger a mostrar los campos
        return ResponseEntity.ok(service.listarPaginado(pageable));
    }

    @GetMapping("/rol/supervisor")
    @PreAuthorize("hasAnyRole('ADMIN', 'TOTAL','ADMINISTRATIVO')")
    @Operation(summary = "Listar todos los supervisores activos")
    public ResponseEntity<List<UsuarioResponseDTO>> listarSupervisores() {
        // Solo devolvemos usuarios activos con rol SUPERVISOR
        return ResponseEntity.ok(service.buscarPorRol(RolUsuario.SUPERVISOR));
    }

    @PutMapping("/{id}/reset-password")
    @PreAuthorize("hasAnyRole('ADMIN', 'TOTAL')")
    public ResponseEntity<UsuarioResponseDTO> resetPassword(
        @PathVariable Long id, 
        @RequestBody ResetPasswordRequest request) { // Usamos el DTO
            return ResponseEntity.ok(service.resetPassword(id, request.nuevaPassword()));
    }
}
