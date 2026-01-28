package com.gestion.erp.modules.auth.services;

import com.gestion.erp.exception.BusinessException;
import com.gestion.erp.exception.EntityNotFoundException;
import com.gestion.erp.exception.ResourceConflictException;
import com.gestion.erp.modules.auth.dtos.ChangePasswordRequestDTO;
import com.gestion.erp.modules.auth.dtos.UsuarioRequestDTO;
import com.gestion.erp.modules.auth.dtos.UsuarioResponseDTO;
import com.gestion.erp.modules.auth.mappers.UsuarioMapper;
import com.gestion.erp.modules.auth.models.Usuario;
import com.gestion.erp.modules.auth.repositories.UsuarioRepository;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import com.gestion.erp.modules.auth.models.enums.RolUsuario;

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
        usuario.setPassword(passwordEncoder.encode(dto.password())); 
        
        // 4. Persistencia
        return repository.save(usuario);
    }

    @Transactional
    public UsuarioResponseDTO eliminarLogico(Long id) {
        Usuario usuario = repository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Usuario no encontrado con ID: " + id));       

        usuario.setActivo(false);
        Usuario actualizado = repository.save(usuario);
        return usuarioMapper.toResponseDTO(actualizado);
    }

    public Page<UsuarioResponseDTO> listarPaginado(Pageable pageable) {
    Page<Usuario> usuarios = repository.findAll(pageable);
    return usuarios.map(usuarioMapper::toResponseDTO);
    }

    @Transactional
    public void changePassword(ChangePasswordRequestDTO request) {
        // 1. Obtener el email del usuario autenticado desde el contexto de seguridad
        String email = SecurityContextHolder.getContext().getAuthentication().getName();
        
        Usuario usuario = repository.findByMailIgnoreCaseAndActivoTrue(email)
                .orElseThrow(() -> new EntityNotFoundException("Usuario no encontrado"));

        // 2. Validar que la contraseña vieja sea correcta
        if (!passwordEncoder.matches(request.getOldPassword(), usuario.getPassword())) {
            throw new BusinessException("La contraseña actual es incorrecta");
        }

        // 3. Validar que la nueva no sea igual a la vieja (Buena práctica de seguridad)
        if (request.getOldPassword().equals(request.getNewPassword())) {
            throw new BusinessException("La nueva contraseña no puede ser igual a la anterior");
        }

        // 4. Encriptar y guardar
        usuario.setPassword(passwordEncoder.encode(request.getNewPassword()));
        repository.save(usuario);
    }

    @Transactional
    public UsuarioResponseDTO resetPassword(Long id, String nuevaPassword) {
        Usuario usuario = repository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Usuario no encontrado con ID: " + id));

        // Encriptar y actualizar la contraseña
        usuario.setPassword(passwordEncoder.encode(nuevaPassword));
        Usuario actualizado = repository.save(usuario);

        return usuarioMapper.toResponseDTO(actualizado);
    }

    public java.util.List<UsuarioResponseDTO> buscarPorRol(RolUsuario rol) {
        java.util.List<Usuario> usuarios = repository.findByRolAndActivoTrue(rol);
        return usuarioMapper.toResponseDTOList(usuarios);
    }
}