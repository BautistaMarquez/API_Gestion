package com.gestion.erp.modules.maestros.mappers;

import com.gestion.erp.modules.maestros.dtos.PrecioInicialDTO;
import com.gestion.erp.modules.maestros.dtos.ProductoPrecioDTO;
import com.gestion.erp.modules.maestros.dtos.ProductoRequestDTO;
import com.gestion.erp.modules.maestros.dtos.ProductoResponseDTO;
import com.gestion.erp.modules.maestros.models.Producto;
import com.gestion.erp.modules.maestros.models.ProductoPrecio;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

import java.util.List;

@Mapper(componentModel = "spring")
public interface ProductoMapper {

    // --- MÉTODO 1: REQUEST -> ENTIDAD (Para Creación) ---
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "precios", source = "precios")
    @Mapping(target = "activo", constant = "true") // Por defecto, al crear es activo
    Producto toEntity(ProductoRequestDTO dto);

    // --- MÉTODO 2: ENTIDAD -> RESPONSE (Para Salida API) ---
    ProductoResponseDTO toResponseDTO(Producto producto);

    // --- MÉTODO 3: LISTA ENTIDAD -> LISTA DTO (Interno para el Response) ---
    // MapStruct lo usa automáticamente al procesar ProductoResponseDTO
    List<ProductoPrecioDTO> toPrecioDTOList(List<ProductoPrecio> precios);

    // Mapeo de un solo precio para la respuesta
    ProductoPrecioDTO toPrecioDTO(ProductoPrecio entidad);

    // --- MÉTODOS DE SOPORTE PARA REQUESTS ---

    // Este es el que faltaba para que el Método 1 no diera error
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "producto", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "updatedAt", ignore = true)
    @Mapping(target = "createdBy", ignore = true)
    ProductoPrecio toPrecioEntity(PrecioInicialDTO dto);

    // Helper method to map list of prices
    default List<ProductoPrecio> mapPrecioList(List<PrecioInicialDTO> precios) {
        if (precios == null) {
            return null;
        }
        return precios.stream()
                .map(this::toPrecioEntity)
                .toList();
    }

    // Mapeo opcional si recibes el DTO completo de precio
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "producto", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "updatedAt", ignore = true)
    @Mapping(target = "createdBy", ignore = true)
    ProductoPrecio toPrecioEntityFromFullDTO(ProductoPrecioDTO dto);

    // --- ACTUALIZACIONES ---
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "precios", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "updatedAt", ignore = true)
    @Mapping(target = "createdBy", ignore = true)
    @Mapping(target = "activo", constant = "true") // Por defecto, al crear es activo
    void updateEntityFromDto(ProductoRequestDTO dto, @MappingTarget Producto producto);
}