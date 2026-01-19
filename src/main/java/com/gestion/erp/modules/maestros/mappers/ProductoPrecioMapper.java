package com.gestion.erp.modules.maestros.mappers;

import com.gestion.erp.modules.maestros.dtos.PrecioInicialDTO;
import com.gestion.erp.modules.maestros.dtos.ProductoPrecioDTO;
import com.gestion.erp.modules.maestros.models.ProductoPrecio;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface ProductoPrecioMapper {

    // Para la creación: Mapea el DTO inicial a la entidad
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "producto", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "updatedAt", ignore = true)
    @Mapping(target = "createdBy", ignore = true)
    ProductoPrecio toEntity(PrecioInicialDTO dto);

    // Para la respuesta: Mapea la entidad al DTO de salida
    @Mapping(target = "producto", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "updatedAt", ignore = true)
    @Mapping(target = "createdBy", ignore = true)
    ProductoPrecio toEntity(ProductoPrecioDTO dto);

    // Para la respuesta: Mapea la entidad al DTO de salida
    ProductoPrecioDTO toDTO(ProductoPrecio entity);
}