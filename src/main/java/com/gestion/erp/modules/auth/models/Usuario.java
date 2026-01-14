package com.gestion.erp.modules.auth.models;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import com.gestion.erp.shared.models.BaseEntity;
import com.gestion.erp.modules.auth.models.enums.RolUsuario;
import com.gestion.erp.modules.maestros.models.Equipo;

@Entity
@Table(name = "usuarios")
@Getter @Setter
@NoArgsConstructor
public class Usuario extends BaseEntity {
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
    private RolUsuario rol; // ADMIN, SUPERVISOR, ADMINISTRATIVO, etc.

    private Boolean activo = true;

    // "mappedBy" le dice a JPA que el dueño de la relación es el campo "supervisor" en la clase Equipo
    @OneToOne(mappedBy = "supervisor", fetch = FetchType.LAZY)
    private Equipo equipo;
}
