package com.gestion.erp.modules.auth.models;

import com.gestion.erp.modules.auth.models.enums.RolUsuario;
import com.gestion.erp.modules.maestros.models.Equipo;
import com.gestion.erp.shared.models.BaseEntity;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.List;

@Entity
@Table(name = "usuarios")
@Getter @Setter
@NoArgsConstructor
public class Usuario extends BaseEntity implements UserDetails { // Implementamos UserDetails
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique = true, nullable = false)
    private String mail;

    @Column(nullable = false)
    private String password;

    @Column(nullable = false)
    private String nombre;

    @Column(nullable = false)
    private String apellido;

    @Enumerated(EnumType.STRING)
    private RolUsuario rol; 

    private Boolean activo = true;

    @OneToOne(mappedBy = "supervisor", fetch = FetchType.LAZY)
    private Equipo equipo;

    // --- Métodos de UserDetails ---

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        // Transformamos nuestro Enum RolUsuario en una autoridad que Spring entienda
        return List.of(new SimpleGrantedAuthority("ROLE_" + rol.name()));
    }

    @Override
    public String getUsername() {
        return mail; // Nuestro "username" contable/logístico es el mail
    }

    @Override
    public boolean isAccountNonExpired() { return true; }

    @Override
    public boolean isAccountNonLocked() { return activo; } // Si activo es false, la cuenta está bloqueada

    @Override
    public boolean isCredentialsNonExpired() { return true; }

    @Override
    public boolean isEnabled() { return activo; }
}
