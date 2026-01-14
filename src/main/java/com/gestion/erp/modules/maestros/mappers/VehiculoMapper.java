package com.gestion.erp.modules.maestros.mappers;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import com.gestion.erp.modules.maestros.dtos.VehiculoRequestDTO;
import com.gestion.erp.modules.maestros.models.Vehiculo;

@Mapper(componentModel = "spring")
public interface VehiculoMapper {
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "estado", constant = "DISPONIBLE")
    @Mapping(target = "version", constant = "0L")
    Vehiculo toEntity(VehiculoRequestDTO dto);
}
