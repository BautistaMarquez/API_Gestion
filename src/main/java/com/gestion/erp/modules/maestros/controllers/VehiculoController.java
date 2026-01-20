package com.gestion.erp.modules.maestros.controllers;

import org.springdoc.core.annotations.ParameterObject;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import jakarta.validation.Valid;

import com.gestion.erp.modules.maestros.dtos.EstadoUpdateRequestDTO;
import com.gestion.erp.modules.maestros.dtos.VehiculoRequestDTO;
import com.gestion.erp.modules.maestros.dtos.VehiculoResponseDTO;
import com.gestion.erp.modules.maestros.models.enums.EstadoVehiculo;
import com.gestion.erp.modules.maestros.services.VehiculoService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/v1/vehiculos")
@RequiredArgsConstructor
public class VehiculoController {
    private final VehiculoService service;

    @PostMapping
    @PreAuthorize("hasAnyRole('ADMINISTRATIVO', 'TOTAL', 'ADMIN')")
    @Operation(summary = "Crear un nuevo vehículo")
    public ResponseEntity<VehiculoResponseDTO> crear(@Valid @RequestBody VehiculoRequestDTO dto) {
        VehiculoResponseDTO response = service.save(dto);
        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }
    
    @DeleteMapping("/{id}")
    @PreAuthorize("hasAnyRole('ADMIN', 'TOTAL')") // Solo roles de alto nivel
    @Operation(summary = "Borrado lógico de un vehículo")
    public ResponseEntity<Void> eliminar(@PathVariable Long id) {
        service.eliminarLogico(id);
        return ResponseEntity.noContent().build(); // Devuelve 204
    }

    @GetMapping
    @PreAuthorize("hasAnyRole('ADMIN', 'TOTAL', 'ADMINISTRATIVO')")
    @Operation(summary = "Listar vehículos con paginación")
    public ResponseEntity<Page<VehiculoResponseDTO>> listarPaginado(
        @ParameterObject Pageable pageable) { // @ParameterObject ayuda a Swagger a mostrar los campos
        return ResponseEntity.ok(service.listarPaginado(pageable));
    }

    @PatchMapping("/{id}/estado")
    @PreAuthorize("hasAnyRole('ADMINISTRATIVO', 'TOTAL', 'ADMIN')")
    @Operation(summary = "Actualizar el estado de un vehículo manualmente")
    public ResponseEntity<VehiculoResponseDTO> updateEstado(
        @PathVariable Long id, 
        @RequestBody EstadoUpdateRequestDTO<EstadoVehiculo> request) {
        return ResponseEntity.ok(service.actualizarEstadoManual(id, request.getNuevoEstado()));
    }
}
