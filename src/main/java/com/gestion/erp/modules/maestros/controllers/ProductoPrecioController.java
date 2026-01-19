package com.gestion.erp.modules.maestros.controllers;
import com.gestion.erp.modules.maestros.dtos.ProductoPrecioDTO;
import com.gestion.erp.modules.maestros.services.ProductoPrecioService;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;


@RestController
@RequestMapping("/api/v1/precios")
@RequiredArgsConstructor
public class ProductoPrecioController {

    private final ProductoPrecioService precioService;

    // Listar todos los precios de un producto específico
    @GetMapping("/producto/{productoId}")
    public ResponseEntity<List<ProductoPrecioDTO>> listarPorProducto(@PathVariable Long productoId) {
        return ResponseEntity.ok(precioService.listarPreciosPorProducto(productoId));
    }

    // Crear un precio vinculado a un producto
    @PostMapping("/producto/{productoId}")
    public ResponseEntity<ProductoPrecioDTO> crear(@PathVariable Long productoId, @Valid @RequestBody ProductoPrecioDTO dto) {
        return new ResponseEntity<>(precioService.crearPrecio(productoId, dto), HttpStatus.CREATED);
    }

    // Actualizar un precio existente
    @PutMapping("/{id}")
    public ResponseEntity<ProductoPrecioDTO> actualizar(@PathVariable Long id, @Valid @RequestBody ProductoPrecioDTO dto) {
        return ResponseEntity.ok(precioService.actualizarPrecio(id, dto));
    }

    // Eliminar un precio
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminar(@PathVariable Long id) {
        precioService.eliminarPrecio(id);
        return ResponseEntity.noContent().build();
    }
}
