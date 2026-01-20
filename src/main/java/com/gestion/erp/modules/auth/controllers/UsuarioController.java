package com.gestion.erp.modules.auth.controllers;

import org.springdoc.core.annotations.ParameterObject;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import jakarta.validation.Valid;
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

    @DeleteMapping("/{id}")
    @PreAuthorize("hasAnyRole('ADMIN', 'TOTAL')") // Solo roles de alto nivel
    @Operation(summary = "Borrado lógico de un usuario")
    public ResponseEntity<Void> eliminar(@PathVariable Long id) {
        service.eliminarLogico(id);
        return ResponseEntity.noContent().build(); // Devuelve 204
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
}
