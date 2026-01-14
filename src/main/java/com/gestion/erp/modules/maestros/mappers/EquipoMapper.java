package com.gestion.erp.modules.maestros.mappers;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import com.gestion.erp.modules.maestros.dtos.EquipoRequestDTO;
import com.gestion.erp.modules.maestros.dtos.EquipoResponseDTO;
import com.gestion.erp.modules.maestros.models.Equipo;

@Mapper(componentModel = "spring")
public interface EquipoMapper {
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "supervisor", ignore = true) // Se orquesta en el Service
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "updatedAt", ignore = true)
    @Mapping(target = "createdBy", ignore = true)
    @Mapping(target = "conductores", ignore = true)
    Equipo toEntity(EquipoRequestDTO dto);

    @Mapping(target = "supervisorNombreCompleto", 
             expression = "java(equipo.getSupervisor().getNombre() + \" \" + equipo.getSupervisor().getApellido())")
    EquipoResponseDTO toResponseDTO(Equipo equipo);
}
