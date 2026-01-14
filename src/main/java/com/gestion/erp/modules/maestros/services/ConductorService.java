package com.gestion.erp.modules.maestros.services;

import org.springframework.stereotype.Service;

import com.gestion.erp.exception.ResourceConflictException;
import com.gestion.erp.modules.maestros.dtos.ConductorRequestDTO;
import com.gestion.erp.modules.maestros.dtos.ConductorResponseDTO;
import com.gestion.erp.modules.maestros.models.Conductor;
import com.gestion.erp.modules.maestros.models.Equipo;
import com.gestion.erp.modules.maestros.models.enums.EstadoConductor;
import com.gestion.erp.exception.EntityNotFoundException;
import com.gestion.erp.modules.maestros.repositories.ConductorRepository;
import com.gestion.erp.modules.maestros.repositories.EquipoRepository;
import com.gestion.erp.modules.maestros.mappers.ConductorMapper;
import org.springframework.transaction.annotation.Transactional;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class ConductorService {
    private final ConductorRepository conductorRepository;
    private final EquipoRepository equipoRepository;
    private final ConductorMapper mapper;

    @Transactional
    public ConductorResponseDTO crear(ConductorRequestDTO dto) {
        if (conductorRepository.existsByDni(dto.dni())) {
            throw new ResourceConflictException("Ya existe un conductor con DNI " + dto.dni());
        }

        Equipo equipo = equipoRepository.findById(dto.equipoId())
            .orElseThrow(() -> new EntityNotFoundException("Equipo no encontrado"));

        Conductor conductor = mapper.toEntity(dto);
        conductor.setEquipo(equipo);

        return mapper.toResponseDTO(conductorRepository.save(conductor));
    }    

    public Conductor validarYObtenerParaViaje(Long id) {
        Conductor c = conductorRepository.findById(id)
            .orElseThrow(() -> new EntityNotFoundException("Conductor no encontrado"));
        if (c.getEstado() != EstadoConductor.DISPONIBLE) {
            throw new ResourceConflictException("El conductor " + c.getNombre() + " no está disponible");
        }
        return c;
    }
}
