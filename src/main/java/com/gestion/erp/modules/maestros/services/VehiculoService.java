package com.gestion.erp.modules.maestros.services;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import com.gestion.erp.exception.BusinessException;
import com.gestion.erp.exception.EntityNotFoundException;
import com.gestion.erp.exception.ResourceConflictException;
import com.gestion.erp.modules.maestros.dtos.VehiculoRequestDTO;
import com.gestion.erp.modules.maestros.dtos.VehiculoResponseDTO;
import com.gestion.erp.modules.maestros.models.Vehiculo;
import com.gestion.erp.modules.maestros.models.enums.EstadoVehiculo;
import com.gestion.erp.modules.maestros.repositories.VehiculoRepository;
import com.gestion.erp.modules.maestros.mappers.VehiculoMapper;
import jakarta.transaction.Transactional;



@Service
@RequiredArgsConstructor
public class VehiculoService {
    private final VehiculoRepository vehiculoRepository;
    private final VehiculoMapper mapper;

    @Transactional
public VehiculoResponseDTO save(VehiculoRequestDTO dto) {
    if (vehiculoRepository.existsByPatenteIgnoreCase(dto.patente())) {
        throw new ResourceConflictException("La patente " + dto.patente() + " ya está registrada");
    }
    
    Vehiculo vehiculo = mapper.toEntity(dto);
    Vehiculo guardado = vehiculoRepository.save(vehiculo);

    return mapper.toResponseDTO(guardado);
}

    public Vehiculo obtenerParaViaje(Long id) {
        Vehiculo v = vehiculoRepository.findById(id)
            .orElseThrow(() -> new EntityNotFoundException("Vehículo no encontrado"));
        
        if (v.getEstado() != EstadoVehiculo.DISPONIBLE) {
            throw new ResourceConflictException("El vehículo " + v.getPatente() + " está " + v.getEstado());
        }
        return v;
    }

    @Transactional
    public void eliminarLogico(Long id) {
     // 1. Buscar el vehículo o lanzar 404
     Vehiculo vehiculo = vehiculoRepository.findById(id)
             .orElseThrow(() -> new EntityNotFoundException("Vehículo no encontrado con ID: " + id));       
     // 2. REGLA DE NEGOCIO: No se puede borrar si está operando
     if (vehiculo.getEstado() == EstadoVehiculo.EN_VIAJE) {
         throw new BusinessException("No se puede eliminar un vehículo que se encuentra EN VIAJE.");
     }      
       
     // 3. Aplicar borrado lógico
     vehiculo.setEstado(EstadoVehiculo.ELIMINADO);

     // 4. Persistir (el @Transactional se encarga del commit)
     vehiculoRepository.save(vehiculo);
    }
}