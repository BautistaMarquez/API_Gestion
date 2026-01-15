package com.gestion.erp.modules.maestros.controllers;

import com.gestion.erp.modules.maestros.dtos.ConductorRequestDTO;
import com.gestion.erp.modules.maestros.dtos.ConductorResponseDTO;
import com.gestion.erp.modules.maestros.services.ConductorService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


@RestController
@RequestMapping("/api/v1/conductores")
@RequiredArgsConstructor
public class ConductorController {
    private final ConductorService service;

    @PostMapping
    public ResponseEntity<ConductorResponseDTO> crear(@Valid @RequestBody ConductorRequestDTO dto) {
        return new ResponseEntity<>(service.crear(dto), HttpStatus.CREATED);
    }
}