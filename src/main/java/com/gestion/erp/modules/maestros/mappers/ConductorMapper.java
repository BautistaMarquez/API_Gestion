package com.gestion.erp.modules.maestros.mappers;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;
import com.gestion.erp.modules.maestros.dtos.ConductorRequestDTO;
import com.gestion.erp.modules.maestros.dtos.ConductorResponseDTO;
import com.gestion.erp.modules.maestros.models.Conductor;
import com.gestion.erp.modules.maestros.models.Equipo;


@Mapper(componentModel = "spring")
public interface ConductorMapper {
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "estado", constant = "DISPONIBLE")
    @Mapping(target = "equipo", ignore = true)
    @Mapping(target = "version", constant = "0L")
    Conductor toEntity(ConductorRequestDTO dto);

    @Mapping(target = "nombreEquipo", source = "equipo", qualifiedByName = "equipoToNombre")
    @Mapping(target = "equipoId", source = "equipo.id")
    @Mapping(target = "supervisorId", source = "equipo.supervisor.id")
    @Mapping(target = "supervisorNombre", source = "equipo.supervisor", qualifiedByName = "supervisorToNombre")
    ConductorResponseDTO toResponseDTO(Conductor conductor);

    @Named("equipoToNombre")
    default String equipoToNombre(Equipo equipo) {
        return equipo != null ? equipo.getNombre() : "";
    }

    @Named("supervisorToNombre")
    default String supervisorToNombre(com.gestion.erp.modules.auth.models.Usuario supervisor) {
        return supervisor != null ? supervisor.getNombre() + " " + supervisor.getApellido() : null;
    }
}