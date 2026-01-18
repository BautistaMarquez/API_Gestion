package com.gestion.erp.modules.auth.mappers;

import com.gestion.erp.modules.auth.dtos.UsuarioRequestDTO;
import com.gestion.erp.modules.auth.models.Usuario;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface UsuarioMapper {

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "activo", constant = "true") // Regla de negocio: usuarios nuevos nacen activos
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "updatedAt", ignore = true)
    @Mapping(target = "createdBy", ignore = true)
    @Mapping(target = "equipo", ignore = true)
    @Mapping(target = "authorities", ignore = true)
    // El password se mapea automáticamente si el nombre coincide, 
    // luego el Service lo sobreescribirá con el hash.
    Usuario toEntity(UsuarioRequestDTO dto);
}