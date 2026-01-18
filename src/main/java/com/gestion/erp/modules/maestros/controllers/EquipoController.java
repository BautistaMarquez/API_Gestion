package com.gestion.erp.modules.maestros.controllers;

import com.gestion.erp.modules.maestros.dtos.EquipoRequestDTO;
import com.gestion.erp.modules.maestros.dtos.EquipoResponseDTO;
import com.gestion.erp.modules.maestros.services.EquipoService;

import io.swagger.v3.oas.annotations.Operation;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

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
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


@RestController
@RequestMapping("/api/v1/equipos")
@RequiredArgsConstructor
public class EquipoController {
    private final EquipoService service;

    @PostMapping
    @PreAuthorize("hasAnyRole('ADMINISTRATIVO', 'TOTAL', 'ADMIN')")
    @Operation(summary = "Crear un nuevo equipo")
    public ResponseEntity<EquipoResponseDTO> crear(@Valid @RequestBody EquipoRequestDTO dto) {
        return new ResponseEntity<>(service.crear(dto), HttpStatus.CREATED);
    }

    @GetMapping
    @PreAuthorize("hasAnyRole('ADMIN', 'TOTAL', 'ADMINISTRATIVO', 'SUPERVISOR')")
    @Operation(summary = "Listar equipos con paginación")
    public ResponseEntity<Page<EquipoResponseDTO>> listarPaginado(
        @ParameterObject Pageable pageable) { // @ParameterObject ayuda a Swagger a mostrar los campos
        return ResponseEntity.ok(service.listarPaginado(pageable));
    }

    @PatchMapping("/{id}/supervisor/{supervisorId}")
    @PreAuthorize("hasAnyRole('ADMIN', 'TOTAL')")
    @Operation(summary = "Asigna o cambia el supervisor de un equipo")
    public ResponseEntity<Void> asignarSupervisor(@PathVariable Long id, @PathVariable Long supervisorId) {
        service.asignarSupervisor(id, supervisorId);
        return ResponseEntity.noContent().build();
    }

    @PatchMapping("/{id}/conductores/{conductorId}")
    @PreAuthorize("hasAnyRole('ADMIN', 'TOTAL', 'ADMINISTRATIVO')")
    @Operation(summary = "Asigna un conductor a un equipo")
    public ResponseEntity<Void> asignarConductor(@PathVariable Long id, @PathVariable Long conductorId) {
        service.agregarConductor(id, conductorId);
        return ResponseEntity.noContent().build();
    }
}
