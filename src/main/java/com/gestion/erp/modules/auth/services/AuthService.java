package com.gestion.erp.modules.auth.services;

import com.gestion.erp.modules.auth.dtos.AuthResponseDTO;
import com.gestion.erp.modules.auth.dtos.LoginRequestDTO;
import com.gestion.erp.modules.auth.models.Usuario;
import com.gestion.erp.modules.auth.repositories.UsuarioRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Map;

@Service
@RequiredArgsConstructor
public class AuthService {

    private final UsuarioRepository usuarioRepository;
    private final JwtService jwtService;
    private final AuthenticationManager authenticationManager;

    public AuthResponseDTO login(LoginRequestDTO request) {
        // 1. Autenticar usando el Manager de Spring (esto usa BCrypt por detrás)
        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        request.email(),
                        request.password()
                )
        );

        // 2. Si llegamos aquí, la clave es correcta. Buscamos al usuario.
        Usuario usuario = usuarioRepository.findByMailIgnoreCase(request.email())
                .orElseThrow(() -> new UsernameNotFoundException("Usuario no encontrado"));

        // 3. Generar el Token con Claims extras (ID y Rol)
        String token = jwtService.generateToken(usuario, Map.of(
                "rol", usuario.getRol().name(),
                "usuarioId", usuario.getId()
        ));

        return new AuthResponseDTO(token, usuario.getMail(), usuario.getRol().name());
    }
}