package com.gestion.erp.modules.maestros.mappers;

import org.mapstruct.Builder;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;
import com.gestion.erp.modules.maestros.dtos.ConductorRequestDTO;
import com.gestion.erp.modules.maestros.dtos.ConductorResponseDTO;
import com.gestion.erp.modules.maestros.models.Conductor;
import com.gestion.erp.modules.maestros.models.Equipo;


@Mapper(
    componentModel = "spring", 
    builder = @Builder(disableBuilder = true)
)
public interface ConductorMapper {
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "estado", constant = "DISPONIBLE")
    @Mapping(target = "equipo", ignore = true)
    @Mapping(target = "version", constant = "0L")
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "updatedAt", ignore = true)
    @Mapping(target = "createdBy", ignore = true)
    Conductor toEntity(ConductorRequestDTO dto);

    @Mapping(target = "nombreEquipo", source = "equipo", qualifiedByName = "equipoToNombre")
    ConductorResponseDTO toResponseDTO(Conductor conductor);

    @Named("equipoToNombre")
    default String equipoToNombre(Equipo equipo) {
        return equipo != null ? equipo.getNombre() : "";
    }
}