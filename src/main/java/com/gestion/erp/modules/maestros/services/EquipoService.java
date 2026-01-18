package com.gestion.erp.modules.maestros.services;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import com.gestion.erp.modules.maestros.dtos.EquipoRequestDTO;
import com.gestion.erp.modules.maestros.dtos.EquipoResponseDTO;
import com.gestion.erp.modules.maestros.mappers.EquipoMapper;
import com.gestion.erp.modules.maestros.models.Equipo;
import com.gestion.erp.modules.maestros.repositories.EquipoRepository;
import com.gestion.erp.modules.auth.models.Usuario;
import com.gestion.erp.modules.auth.repositories.UsuarioRepository;
import com.gestion.erp.exception.EntityNotFoundException;
import com.gestion.erp.exception.ResourceConflictException;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class EquipoService {
    private final EquipoRepository repository;
    private final UsuarioRepository usuarioRepository;
    private final EquipoMapper mapper;

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
}
