package com.gestion.erp.modules.maestros.services;
import com.gestion.erp.modules.maestros.dtos.ProductoPrecioDTO;
import com.gestion.erp.modules.maestros.models.Producto;
import com.gestion.erp.modules.maestros.models.ProductoPrecio;
import com.gestion.erp.modules.maestros.repositories.ProductoPrecioRepository;
import com.gestion.erp.modules.maestros.repositories.ProductoRepository;
import com.gestion.erp.modules.maestros.mappers.ProductoPrecioMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import com.gestion.erp.exception.*;
import java.util.List;
import java.util.stream.Collectors;




@Service
@RequiredArgsConstructor
@Slf4j
public class ProductoPrecioService {

    private final ProductoPrecioRepository precioRepository;
    private final ProductoRepository productoRepository;
    private final ProductoPrecioMapper precioMapper;

    @Transactional
    public ProductoPrecioDTO crearPrecio(Long productoId, ProductoPrecioDTO dto) {
        Producto producto = productoRepository.findById(productoId)
                .orElseThrow(() -> new EntityNotFoundException("Producto no encontrado"));

        ProductoPrecio nuevoPrecio = precioMapper.toEntity(dto);
        nuevoPrecio.setProducto(producto);

        return precioMapper.toDTO(precioRepository.save(nuevoPrecio));
    }

    @Transactional
    public ProductoPrecioDTO actualizarPrecio(Long id, ProductoPrecioDTO dto) {
        ProductoPrecio precioExistente = precioRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Precio no encontrado"));

        // Actualizamos campos permitidos
        precioExistente.setEtiqueta(dto.etiqueta());
        precioExistente.setValor(dto.valor());

        return precioMapper.toDTO(precioRepository.save(precioExistente));
    }

    @Transactional
    public void eliminarPrecio(Long id) {
        if (!precioRepository.existsById(id)) {
            throw new EntityNotFoundException("Precio no encontrado");
        }
        // Borrado físico: Permitido gracias al snapshot en ViajeDetalle
        precioRepository.deleteById(id);
        log.warn("Se ha eliminado físicamente el precio ID: {}", id);
    }
    
    @Transactional(readOnly = true)
    public List<ProductoPrecioDTO> listarPreciosPorProducto(Long productoId) {
        return precioRepository.findByProductoId(productoId) // Necesitarás este método en el Repository
                .stream()
                .map(precioMapper::toDTO)
                .collect(Collectors.toList());
    }
}