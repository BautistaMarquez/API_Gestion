package com.gestion.erp.modules.maestros.services;

import com.gestion.erp.modules.maestros.dtos.ProductoRequestDTO;
import com.gestion.erp.modules.maestros.dtos.ProductoResponseDTO;
import com.gestion.erp.modules.maestros.mappers.ProductoMapper;
import com.gestion.erp.modules.maestros.models.Producto;
import com.gestion.erp.modules.maestros.repositories.ProductoRepository;
import com.gestion.erp.exception.EntityNotFoundException;
import com.gestion.erp.exception.ResourceConflictException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;

@Service
@RequiredArgsConstructor
public class ProductoService {

    private final ProductoRepository productoRepository;
    private final ProductoMapper productoMapper; // Inyectamos el Mapper

    @Transactional
    public ProductoResponseDTO crearProducto(ProductoRequestDTO dto) {
        // 1. Regla de Negocio: Validar nombre único
        if (productoRepository.existsByNombreIgnoreCase(dto.getNombre())) {
            throw new ResourceConflictException("El producto con nombre " + dto.getNombre() + " ya existe");
        }

        // 2. Uso de MapStruct: De DTO a Entidad
        Producto producto = productoMapper.toEntity(dto);

        // 3. Vincular relación Bidireccional (Lógica que el mapper no hace solo por seguridad)
        if (producto.getPrecios() != null) {
            producto.getPrecios().forEach(precio -> precio.setProducto(producto));
        }

        Producto guardado = productoRepository.save(producto);

        // 4. De Entidad a ResponseDTO
        return productoMapper.toResponseDTO(guardado);
    }

    public List<ProductoResponseDTO> listarTodos() {
        return productoRepository.findAll().stream()
                .map(productoMapper::toResponseDTO) // Limpio y directo
                .toList();
    }

    @Transactional
    public void eliminarLogico(Long id) {
     // 1. Buscar el producto o lanzar 404
     Producto producto = productoRepository.findById(id)
             .orElseThrow(() -> new EntityNotFoundException("Producto no encontrado con ID: " + id));       

     // 2. Aplicar borrado lógico
     producto.setActivo(false);

     // 3. Persistir (el @Transactional se encarga del commit)
     productoRepository.save(producto);
    }
}