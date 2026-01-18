package com.gestion.erp.modules.auth.services;

import com.gestion.erp.exception.ResourceConflictException;
import com.gestion.erp.modules.auth.dtos.UsuarioRequestDTO;
import com.gestion.erp.modules.auth.mappers.UsuarioMapper;
import com.gestion.erp.modules.auth.models.Usuario;
import com.gestion.erp.modules.auth.repositories.UsuarioRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UsuarioService {
    private final UsuarioRepository repository;
    private final UsuarioMapper usuarioMapper;
    private final PasswordEncoder passwordEncoder;

    @Transactional
    public Usuario registrar(UsuarioRequestDTO dto) {
        // 1. Validación de unicidad
        if (repository.existsByMailIgnoreCase(dto.mail())) {
            throw new ResourceConflictException("El email " + dto.mail() + " ya está en uso");
        }
        
        // 2. Mapeo a entidad
        Usuario usuario = usuarioMapper.toEntity(dto);

        // 3. Hashing de Password (BCrypt)
        // Nunca guardamos texto plano. BCrypt incluye un "salt" aleatorio automáticamente.
        usuario.setPassword(passwordEncoder.encode(dto.password())); 
        
        // 4. Persistencia
        return repository.save(usuario);
    }
}