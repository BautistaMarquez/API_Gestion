package com.gestion.erp.modules.logistica.controllers;

import com.gestion.erp.modules.logistica.dtos.ViajeCierreRequestDTO;
import com.gestion.erp.modules.logistica.dtos.ViajeRequestDTO;
import com.gestion.erp.modules.logistica.dtos.ViajeResponseDTO;
import com.gestion.erp.modules.logistica.services.ViajeService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

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
    public ResponseEntity<ViajeResponseDTO> finalizarViaje(@Valid @RequestBody ViajeCierreRequestDTO request) {
        ViajeResponseDTO response = viajeService.finalizarViaje(request);
        return ResponseEntity.ok(response);
    }
}