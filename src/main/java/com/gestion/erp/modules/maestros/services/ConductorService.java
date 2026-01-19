package com.gestion.erp.modules.maestros.services;

import java.time.LocalDate;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.gestion.erp.exception.ResourceConflictException;
import com.gestion.erp.modules.maestros.dtos.ConductorRequestDTO;
import com.gestion.erp.modules.maestros.dtos.ConductorResponseDTO;
import com.gestion.erp.modules.maestros.models.Conductor;
import com.gestion.erp.modules.maestros.models.Equipo;
import com.gestion.erp.modules.maestros.models.enums.EstadoConductor;
import com.gestion.erp.exception.BusinessException;
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
        // Regla de Oro: Validar vencimiento de licencia
        if (c.getLicenciaVencimiento().isBefore(LocalDate.now())) {
            throw new BusinessException("La licencia del conductor " + c.getNombre() + " está vencida");
        }
        
        return c;
    }

    @Transactional
    public void eliminarLogico(Long id) {
     // 1. Buscar el conductor o lanzar 404
     Conductor conductor = conductorRepository.findById(id)
             .orElseThrow(() -> new EntityNotFoundException("Conductor no encontrado con ID: " + id)); 

     // 2. REGLA DE NEGOCIO: No se puede borrar si está operando
     if (conductor.getEstado() == EstadoConductor.OCUPADO) {
         throw new BusinessException("No se puede eliminar un conductor que se encuentra OCUPADO.");
     }      
     // 3. Aplicar borrado lógico
     conductor.setEstado(EstadoConductor.ELIMINADO);

     // 4. Persistir (el @Transactional se encarga del commit)
     conductorRepository.save(conductor);
    }

    public Page<ConductorResponseDTO> listarPaginado(Pageable pageable) {
    Page<Conductor> conductores = conductorRepository.findAll(pageable);
    // La ventaja de Page es que tiene un método .map() muy potente
    return conductores.map(mapper::toResponseDTO);
    }

    @Transactional
    public ConductorResponseDTO actualizarEstadoManual(Long id, EstadoConductor nuevoEstado) {
    Conductor conductor = conductorRepository.findById(id)
            .orElseThrow(() -> new EntityNotFoundException("Conductor no encontrado"));

    // REGLA DE NEGOCIO: No se puede cambiar el estado si está ocupado
    if (conductor.getEstado() == EstadoConductor.OCUPADO) {
        throw new BusinessException("No se puede cambiar el estado de un conductor que está OCUPADO en un viaje.");
    }

    conductor.setEstado(nuevoEstado);
    return mapper.toResponseDTO(conductorRepository.save(conductor));
    }
}
