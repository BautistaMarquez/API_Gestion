package com.gestion.erp.modules.auth.services;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import com.gestion.erp.exception.ResourceConflictException;
import com.gestion.erp.modules.auth.dtos.UsuarioRequestDTO;
import com.gestion.erp.modules.auth.models.Usuario;
import com.gestion.erp.modules.auth.repositories.UsuarioRepository;
import jakarta.transaction.Transactional;
import com.gestion.erp.modules.auth.mappers.UsuarioMapper; // Importamos el nuevo mapper


@Service
@RequiredArgsConstructor
public class UsuarioService {
    private final UsuarioRepository repository;
    private final UsuarioMapper usuarioMapper; // Inyectamos el nuevo mapper

    @Transactional
    public Usuario registrar(UsuarioRequestDTO dto) {
        // 1. Validación de unicidad (Regla de negocio)
        if (repository.existsByMailIgnoreCase(dto.mail())) {
            throw new ResourceConflictException("El email " + dto.mail() + " ya está en uso");
        }
        
        // 2. Mapeo profesional
        Usuario usuario = usuarioMapper.toEntity(dto);

        // 3. Tratamiento de Seguridad (Punto crítico)
        // TODO: Implementar passwordEncoder.encode(dto.password()) cuando configuremos Security
        usuario.setPassword(dto.password()); 
        
        // 4. Persistencia
        return repository.save(usuario);
    }
}