package com.gestion.erp.modules.logistica.mappers;

import com.gestion.erp.modules.logistica.dtos.DetalleRequestDTO;
import com.gestion.erp.modules.logistica.models.ViajeDetalle;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface ViajeDetalleMapper {

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "viaje", ignore = true)
    @Mapping(target = "producto", ignore = true) // Se asigna en el Service vía ID
    @Mapping(target = "cantidadFinal", ignore = true) // No existe al inicio
    @Mapping(target = "ventaRealizada", constant = "0") // Valor por defecto contable
    @Mapping(target = "precioAplicado", ignore = true) // Se busca el snapshot en el Service
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "updatedAt", ignore = true)
    @Mapping(target = "createdBy", ignore = true)
    ViajeDetalle toEntity(DetalleRequestDTO dto);
}