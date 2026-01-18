package com.gestion.erp.modules.logistica.controllers;

import com.gestion.erp.modules.logistica.dtos.ViajeCierreRequestDTO;
import com.gestion.erp.modules.logistica.dtos.ViajeRequestDTO;
import com.gestion.erp.modules.logistica.dtos.ViajeResponseDTO;
import com.gestion.erp.modules.logistica.services.ViajeService;

import io.swagger.v3.oas.annotations.Operation;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

import org.springdoc.core.annotations.ParameterObject;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/api/v1/viajes")
@RequiredArgsConstructor
public class ViajeController {

    private final ViajeService viajeService;

    /**
     * HU #1: Registro de Inicio de Viaje.
     * Creamos un nuevo recurso "Viaje" y devolvemos 201 Created.
     */
    @PostMapping
    @PreAuthorize("hasAnyRole('SUPERSIVOR_PLANTA','ADMIN','TOTAL')")
    public ResponseEntity<ViajeResponseDTO> iniciarViaje(@Valid @RequestBody ViajeRequestDTO request) {
        ViajeResponseDTO response = viajeService.registrarInicioViaje(request);
        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }

    /**
     * HU #2: Registro de Final de Viaje (Liquidación).
     * Usamos PATCH porque estamos actualizando parcialmente el estado del viaje
     * (de EN_PROCESO a FINALIZADO) y completando datos de cierre.
     */
    @PatchMapping("/finalizar")
    @PreAuthorize("hasAnyRole('SUPERSIVOR_PLANTA','ADMIN','TOTAL')")
    public ResponseEntity<ViajeResponseDTO> finalizarViaje(@Valid @RequestBody ViajeCierreRequestDTO request) {
        ViajeResponseDTO response = viajeService.finalizarViaje(request);
        return ResponseEntity.ok(response);
    }

    /**
     * Listar Viajes.
     * Permite listar todos los viajes o filtrar por supervisor según el rol del usuario.
     */
    @GetMapping
    @PreAuthorize("hasAnyRole('SUPERSIVOR','ADMIN','TOTAL')")
    public ResponseEntity<List<ViajeResponseDTO>> listarViajes() {
        List<ViajeResponseDTO> viajes = viajeService.listarViajes();
        return ResponseEntity.ok(viajes);
    }  

    @GetMapping
    @PreAuthorize("hasAnyRole('ADMIN', 'TOTAL', 'ADMINISTRATIVO')")
    @Operation(summary = "Listar vehículos con paginación")
    public ResponseEntity<Page<ViajeResponseDTO>> listarPaginado(
        @ParameterObject Pageable pageable) { // @ParameterObject ayuda a Swagger a mostrar los campos
        return ResponseEntity.ok(viajeService.listarPaginado(pageable));
    }
}