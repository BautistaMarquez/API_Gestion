package com.gestion.erp.modules.maestros.services;

import java.util.stream.Collectors;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.gestion.erp.modules.maestros.dtos.EquipoDetalleResponseDTO;
import com.gestion.erp.modules.maestros.dtos.EquipoRequestDTO;
import com.gestion.erp.modules.maestros.dtos.EquipoResponseDTO;
import com.gestion.erp.modules.maestros.mappers.ConductorMapper;
import com.gestion.erp.modules.maestros.mappers.EquipoMapper;
import com.gestion.erp.modules.maestros.models.Conductor;
import com.gestion.erp.modules.maestros.models.Equipo;
import com.gestion.erp.modules.maestros.models.enums.EstadoConductor;
import com.gestion.erp.modules.maestros.repositories.ConductorRepository;
import com.gestion.erp.modules.maestros.repositories.EquipoRepository;
import com.gestion.erp.modules.auth.mappers.UsuarioMapper;
import com.gestion.erp.modules.auth.models.Usuario;
import com.gestion.erp.modules.auth.models.enums.RolUsuario;
import com.gestion.erp.modules.auth.repositories.UsuarioRepository;
import com.gestion.erp.exception.BusinessException;
import com.gestion.erp.exception.EntityNotFoundException;
import com.gestion.erp.exception.ResourceConflictException;
import lombok.RequiredArgsConstructor;
import java.util.List;

@Service
@RequiredArgsConstructor
public class EquipoService {
    private final EquipoRepository repository;
    private final UsuarioRepository usuarioRepository;
    private final EquipoMapper mapper;
    private final ConductorRepository conductorRepository;
    private final ConductorMapper conductorMapper;
    private final UsuarioMapper usuarioMapper;

    @Transactional
    public EquipoResponseDTO crear(EquipoRequestDTO dto) {
        if (repository.existsByNombreIgnoreCase(dto.nombre())) {
            throw new ResourceConflictException("El equipo " + dto.nombre() + " ya existe");
        }

        Usuario supervisor = usuarioRepository.findById(dto.supervisorId())
            .orElseThrow(() -> new EntityNotFoundException("Supervisor no encontrado"));

        // Regla de Negocio: El usuario debe tener rol de SUPERVISOR (Auditoría)
        // Nota: Aquí podrías validar el Enum RolUsuario
        
        Equipo equipo = mapper.toEntity(dto);
        equipo.setSupervisor(supervisor);
        
        return mapper.toResponseDTO(repository.save(equipo));
    }

    public Page<EquipoResponseDTO> listarPaginado(Pageable pageable) {
        Page<Equipo> equipos = repository.findAll(pageable);
        // La ventaja de Page es que tiene un método .map() muy potente
        return equipos.map(mapper::toResponseDTO);
    }

    @Transactional
    public void asignarSupervisor(Long equipoId, Long supervisorId) {
        Equipo equipo = repository.findById(equipoId)
                .orElseThrow(() -> new EntityNotFoundException("Equipo no encontrado"));

        Usuario supervisor = usuarioRepository.findById(supervisorId)
                .orElseThrow(() -> new EntityNotFoundException("Usuario no encontrado"));

        // 1. Validar que el usuario tenga el ROL correcto
        if (supervisor.getRol() != RolUsuario.SUPERVISOR) {
            throw new BusinessException("El usuario seleccionado no tiene el rol de SUPERVISOR.");
        }

        // 2. Validar que el supervisor no esté ya asignado a OTRO equipo (Regla 1:1)
        // Suponiendo que tienes un método findBySupervisorId en EquipoRepository
        repository.findBySupervisorId(supervisorId).ifPresent(e -> {
            if (!e.getId().equals(equipoId)) {
                throw new BusinessException("Este supervisor ya está a cargo del equipo: " + e.getNombre());
            }
        });

        equipo.setSupervisor(supervisor);
        repository.save(equipo);
    }

    @Transactional
    public void agregarConductor(Long equipoId, Long conductorId) {
        Equipo equipo = repository.findById(equipoId)
            .orElseThrow(() -> new EntityNotFoundException("Equipo no encontrado"));

        Conductor conductor = conductorRepository.findById(conductorId)
            .orElseThrow(() -> new EntityNotFoundException("Conductor no encontrado"));

        // Regla de Negocio: No mover conductores que están trabajando
        if (conductor.getEstado() == EstadoConductor.OCUPADO) {
        throw new BusinessException("No se puede reasignar un conductor que está actualmente OCUPADO en un viaje.");
        }
        conductor.setEquipo(equipo);
        conductorRepository.save(conductor); // Actualizamos el dueño de la relación
    }

    @Transactional(readOnly = true)
    public EquipoDetalleResponseDTO obtenerDetalleEquipo(Long id) {
        // 1. Buscamos el equipo
        Equipo equipo = repository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Equipo no encontrado"));

        // 2. Buscamos los conductores asociados a este equipo
        List<Conductor> conductores = conductorRepository.findByEquipoId(id);

        // 3. Mapeamos todo al DTO de detalle
        EquipoDetalleResponseDTO detalle = new EquipoDetalleResponseDTO();
        detalle.setId(equipo.getId());
        detalle.setNombre(equipo.getNombre());
        detalle.setSupervisor(usuarioMapper.toResponseDTO(equipo.getSupervisor()));
        detalle.setConductores(conductores.stream()
                .map(conductorMapper::toResponseDTO)
                .collect(Collectors.toList()));

        return detalle;
    }
}
