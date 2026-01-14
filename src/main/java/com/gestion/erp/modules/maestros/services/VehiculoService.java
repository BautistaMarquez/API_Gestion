package com.gestion.erp.modules.maestros.services;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import com.gestion.erp.exception.EntityNotFoundException;
import com.gestion.erp.exception.ResourceConflictException;
import com.gestion.erp.modules.maestros.models.Vehiculo;
import com.gestion.erp.modules.maestros.models.enums.EstadoVehiculo;
import com.gestion.erp.modules.maestros.repositories.VehiculoRepository;

@Service
@RequiredArgsConstructor
public class VehiculoService {
    private final VehiculoRepository vehiculoRepository;

    public Vehiculo obtenerParaViaje(Long id) {
        Vehiculo v = vehiculoRepository.findById(id)
            .orElseThrow(() -> new EntityNotFoundException("Vehículo no encontrado"));
        
        if (v.getEstado() != EstadoVehiculo.DISPONIBLE) {
            throw new ResourceConflictException("El vehículo " + v.getPatente() + " está " + v.getEstado());
        }
        return v;
    }
}
