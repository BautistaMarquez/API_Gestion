package com.gestion.erp.modules.maestros.services;

import com.gestion.erp.modules.maestros.dtos.ProductoRequestDTO;
import com.gestion.erp.modules.maestros.dtos.ProductoResponseDTO;
import com.gestion.erp.modules.maestros.models.Producto;
import com.gestion.erp.modules.maestros.repositories.ProductoRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class ProductoService {

    private final ProductoRepository productoRepository;

    @Transactional
    public ProductoResponseDTO crearProducto(ProductoRequestDTO dto) {
        // Regla de Negocio #1: Validar nombre único
        if (productoRepository.findByNombre(dto.nombre()).isPresent()) {
            throw new RuntimeException("Ya existe un producto registrado con el nombre: " + dto.nombre());
        }

        // Mapeo Manual (Por ahora, luego usaremos MapStruct)
        Producto nuevoProducto = Producto.builder()
                .nombre(dto.nombre())
                .build();

        Producto guardado = productoRepository.save(nuevoProducto);

        // Retornamos el DTO de respuesta
        return new ProductoResponseDTO(
                guardado.getId(), 
                guardado.getNombre()
        );
    }

    @Transactional(readOnly = true)
    public java.util.List<ProductoResponseDTO> listarTodos() {
        var productos = productoRepository.findAll();
        var responseList = new java.util.ArrayList<ProductoResponseDTO>();

        for (Producto producto : productos) {
            responseList.add(new ProductoResponseDTO(
                    producto.getId(),
                    producto.getNombre()
            ));
        }

        return responseList;
    }
}