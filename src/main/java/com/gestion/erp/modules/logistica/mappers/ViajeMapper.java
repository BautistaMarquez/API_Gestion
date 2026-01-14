package com.gestion.erp.modules.logistica.mappers;

import com.gestion.erp.modules.logistica.dtos.DetalleResponseDTO;
import com.gestion.erp.modules.logistica.dtos.ViajeRequestDTO;
import com.gestion.erp.modules.logistica.dtos.ViajeResponseDTO;
import com.gestion.erp.modules.logistica.models.Viaje;
import com.gestion.erp.modules.logistica.models.ViajeDetalle;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring", uses = {ViajeDetalleMapper.class})
public interface ViajeMapper {

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "fechaInicio", expression = "java(java.time.LocalDateTime.now())")
    @Mapping(target = "fechaFin", ignore = true)
    @Mapping(target = "estado", constant = "EN_PROCESO")
    @Mapping(target = "ventaTotal", expression = "java(java.math.BigDecimal.ZERO)")
    @Mapping(target = "vehiculo", ignore = true)   // Se resuelven por ID en el Service
    @Mapping(target = "conductor", ignore = true)  // Se resuelven por ID en el Service
    @Mapping(target = "supervisor", ignore = true) // Se resuelven por ID en el Service
    @Mapping(target = "version", ignore = true)
    Viaje toEntity(ViajeRequestDTO dto);

    @Mapping(target = "vehiculoPatente", source = "vehiculo.patente")
    @Mapping(target = "conductorNombre", expression = "java(viaje.getConductor().getNombre() + \" \" + viaje.getConductor().getApellido())")
    @Mapping(target = "supervisorNombre", source = "supervisor.nombre")
    ViajeResponseDTO toResponseDTO(Viaje viaje);

    @Mapping(target = "productoId", source = "producto.id")
    @Mapping(target = "productoNombre", source = "producto.nombre")
    DetalleResponseDTO toDetalleResponseDTO(ViajeDetalle detalle);

    
}