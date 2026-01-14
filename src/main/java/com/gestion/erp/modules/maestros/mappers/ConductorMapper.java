package com.gestion.erp.modules.maestros.mappers;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import com.gestion.erp.modules.maestros.dtos.ConductorRequestDTO;
import com.gestion.erp.modules.maestros.dtos.ConductorResponseDTO;
import com.gestion.erp.modules.maestros.models.Conductor;

@Mapper(componentModel = "spring")
public interface ConductorMapper {
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "estado", constant = "DISPONIBLE")
    @Mapping(target = "equipo", ignore = true)
    @Mapping(target = "version", constant = "0L")
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "updatedAt", ignore = true)
    @Mapping(target = "createdBy", ignore = true)
    Conductor toEntity(ConductorRequestDTO dto);

    @Mapping(target = "nombreEquipo", source = "equipo.nombre")
    ConductorResponseDTO toResponseDTO(Conductor conductor);
}