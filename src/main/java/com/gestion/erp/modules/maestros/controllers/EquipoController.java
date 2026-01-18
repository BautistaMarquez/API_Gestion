package com.gestion.erp.modules.maestros.controllers;

import com.gestion.erp.modules.maestros.dtos.EquipoRequestDTO;
import com.gestion.erp.modules.maestros.dtos.EquipoResponseDTO;
import com.gestion.erp.modules.maestros.services.EquipoService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
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
    public ResponseEntity<EquipoResponseDTO> crear(@Valid @RequestBody EquipoRequestDTO dto) {
        return new ResponseEntity<>(service.crear(dto), HttpStatus.CREATED);
    }
}
