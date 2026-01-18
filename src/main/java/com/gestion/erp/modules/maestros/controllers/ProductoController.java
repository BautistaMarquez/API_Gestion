package com.gestion.erp.modules.maestros.controllers;

import com.gestion.erp.modules.maestros.dtos.ProductoRequestDTO;
import com.gestion.erp.modules.maestros.dtos.ProductoResponseDTO;
import com.gestion.erp.modules.maestros.services.ProductoService;

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
    @PreAuthorize("hasAnyRole('ADMINISTRATIVO', 'TOTAL', 'ADMIN')")
    public ResponseEntity<ProductoResponseDTO> crearProducto(@Valid @RequestBody ProductoRequestDTO request) {
        ProductoResponseDTO response = productoService.crearProducto(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    /**
     * Endpoint para obtener el catálogo completo de productos.
     */
    @GetMapping
    @PreAuthorize("hasAnyRole('ADMINISTRATIVO', 'TOTAL', 'ADMIN')")
    public ResponseEntity<List<ProductoResponseDTO>> listarProductos() {
        return ResponseEntity.ok(productoService.listarTodos());
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasAnyRole('ADMIN', 'TOTAL')") // Solo roles de alto nivel
    @Operation(summary = "Borrado lógico de un producto")
    public ResponseEntity<Void> eliminar(@PathVariable Long id) {
        productoService.eliminarLogico(id);
        return ResponseEntity.noContent().build(); // Devuelve 204
    }

        @GetMapping
    @PreAuthorize("hasAnyRole('ADMIN', 'TOTAL', 'ADMINISTRATIVO')")
    @Operation(summary = "Listar productos con paginación")
    public ResponseEntity<Page<ProductoResponseDTO>> listarPaginado(
        @ParameterObject Pageable pageable) { // @ParameterObject ayuda a Swagger a mostrar los campos
        return ResponseEntity.ok(productoService.listarPaginado(pageable));
    }
}
