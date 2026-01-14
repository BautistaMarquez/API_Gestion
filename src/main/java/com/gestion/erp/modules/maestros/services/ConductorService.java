package com.gestion.erp.modules.maestros.services;

import org.springframework.stereotype.Service;

import com.gestion.erp.exception.ResourceConflictException;
import com.gestion.erp.modules.maestros.models.Conductor;
import com.gestion.erp.modules.maestros.models.enums.EstadoConductor;
import com.gestion.erp.exception.EntityNotFoundException;
import com.gestion.erp.modules.maestros.repositories.ConductorRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class ConductorService {
    private final ConductorRepository conductorRepository;

    public Conductor validarYObtenerParaViaje(Long id) {
        Conductor c = conductorRepository.findById(id)
            .orElseThrow(() -> new EntityNotFoundException("Conductor no encontrado"));
        if (c.getEstado() != EstadoConductor.DISPONIBLE) {
            throw new ResourceConflictException("El conductor " + c.getNombre() + " no está disponible");
        }
        return c;
    }
}
