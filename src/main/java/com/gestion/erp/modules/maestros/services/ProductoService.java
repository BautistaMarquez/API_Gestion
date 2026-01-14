package com.gestion.erp.modules.maestros.services;

import com.gestion.erp.modules.maestros.dtos.ProductoRequestDTO;
import com.gestion.erp.modules.maestros.dtos.ProductoResponseDTO;
import com.gestion.erp.modules.maestros.dtos.ProductoPrecioDTO;
import com.gestion.erp.modules.maestros.dtos.PrecioInicialDTO;
import com.gestion.erp.modules.maestros.models.Producto;
import com.gestion.erp.modules.maestros.models.ProductoPrecio;
import com.gestion.erp.modules.maestros.repositories.ProductoRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

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

        // Mapeo Manual: crear Producto con precios iniciales
        Producto nuevoProducto = Producto.builder()
                .nombre(dto.nombre())
                .activo(true) // Activo por defecto
                .precios(new ArrayList<>())
                .build();

        // Mapear PrecioInicialDTO a ProductoPrecio
        for (PrecioInicialDTO precioDto : dto.precios()) {
            ProductoPrecio productoPrecio = new ProductoPrecio();
            productoPrecio.setEtiqueta(precioDto.etiqueta());
            productoPrecio.setValor(precioDto.valor());
            nuevoProducto.addPrecio(productoPrecio);
        }

        Producto guardado = productoRepository.save(nuevoProducto);

        // Retornamos el DTO de respuesta mapeado
        return mapToResponseDTO(guardado);
    }

    @Transactional(readOnly = true)
    public List<ProductoResponseDTO> listarTodos() {
        var productos = productoRepository.findAll();
        return productos.stream()
                .map(this::mapToResponseDTO)
                .collect(Collectors.toList());
    }

    /**
     * Mapea una entidad Producto a ProductoResponseDTO
     */
    private ProductoResponseDTO mapToResponseDTO(Producto producto) {
        List<ProductoPrecioDTO> preciosDtos = producto.getPrecios().stream()
                .map(precio -> new ProductoPrecioDTO(
                        precio.getId(),
                        precio.getEtiqueta(),
                        precio.getValor()
                ))
                .collect(Collectors.toList());

        return new ProductoResponseDTO(
                producto.getId(),
                producto.getNombre(),
                producto.getActivo(),
                preciosDtos
        );
    }
}