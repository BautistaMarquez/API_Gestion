package com.gestion.erp.modules.maestros.controllers;
import com.gestion.erp.modules.maestros.dtos.ProductoPrecioDTO;
import com.gestion.erp.modules.maestros.services.ProductoPrecioService;

import io.swagger.v3.oas.annotations.Operation;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import java.util.List;


@RestController
@RequestMapping("/api/v1/precios")
@RequiredArgsConstructor
public class ProductoPrecioController {

    private final ProductoPrecioService precioService;

    
    @GetMapping("/producto/{productoId}")
    @PreAuthorize("hasAnyRole('ADMINISTRATIVO', 'TOTAL', 'ADMIN')")
    @Operation(summary = "Listar precios por producto")
    public ResponseEntity<List<ProductoPrecioDTO>> listarPorProducto(@PathVariable Long productoId) {
        return ResponseEntity.ok(precioService.listarPreciosPorProducto(productoId));
    }

    
    @PostMapping("/producto/{productoId}")
    @PreAuthorize("hasAnyRole('ADMINISTRATIVO', 'TOTAL', 'ADMIN')")
    @Operation(summary = "Crear un nuevo precio para un producto")
    public ResponseEntity<ProductoPrecioDTO> crear(@PathVariable Long productoId, @Valid @RequestBody ProductoPrecioDTO dto) {
        return new ResponseEntity<>(precioService.crearPrecio(productoId, dto), HttpStatus.CREATED);
    }

    
    @PutMapping("/{id}")
    @PreAuthorize("hasAnyRole('ADMINISTRATIVO', 'TOTAL', 'ADMIN')")
    @Operation(summary = "Actualizar un precio existente")
    public ResponseEntity<ProductoPrecioDTO> actualizar(@PathVariable Long id, @Valid @RequestBody ProductoPrecioDTO dto) {
        return ResponseEntity.ok(precioService.actualizarPrecio(id, dto));
    }

    
    @DeleteMapping("/{id}")
    @PreAuthorize("hasAnyRole('ADMIN', 'TOTAL', 'ADMINISTRATIVO')")
    @Operation(summary = "Eliminar un precio de producto")
    public ResponseEntity<Void> eliminar(@PathVariable Long id) {
        precioService.eliminarPrecio(id);
        return ResponseEntity.noContent().build();
    }
}
