package com.gestion.erp.modules.maestros.controllers;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import jakarta.validation.Valid;
import com.gestion.erp.modules.maestros.dtos.VehiculoRequestDTO;
import com.gestion.erp.modules.maestros.models.Vehiculo;
import com.gestion.erp.modules.maestros.services.VehiculoService;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/v1/vehiculos")
@RequiredArgsConstructor
public class VehiculoController {
    private final VehiculoService service;

    @PostMapping
    public ResponseEntity<Vehiculo> crear(@Valid @RequestBody VehiculoRequestDTO dto) {
        return new ResponseEntity<>(service.save(dto), HttpStatus.CREATED);
    }
}
