package com.gestion.erp.modules.maestros.controllers;

import com.gestion.erp.modules.maestros.dtos.ProductoRequestDTO;
import com.gestion.erp.modules.maestros.dtos.ProductoResponseDTO;
import com.gestion.erp.modules.maestros.services.ProductoService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * Controlador REST para la gestión del maestro de productos.
 * Sigue las convenciones de nomenclatura plural para recursos.
 */
@RestController
@RequestMapping("/api/v1/productos")
@RequiredArgsConstructor
public class ProductoController {

    private final ProductoService productoService;

    /**
     * Endpoint para dar de alta un nuevo producto.
     * @param request Datos del producto validados.
     * @return El producto creado con su ID y código 201 Created.
     */
    @PostMapping
    public ResponseEntity<ProductoResponseDTO> crearProducto(@Valid @RequestBody ProductoRequestDTO request) {
        ProductoResponseDTO response = productoService.crearProducto(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    /**
     * Endpoint para obtener el catálogo completo de productos.
     */
    @GetMapping
    public ResponseEntity<List<ProductoResponseDTO>> listarProductos() {
        return ResponseEntity.ok(productoService.listarTodos());
    }
}
