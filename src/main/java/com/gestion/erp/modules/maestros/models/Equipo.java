package com.gestion.erp.modules.maestros.models;
import java.util.ArrayList;
import java.util.List;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import com.gestion.erp.modules.auth.models.Usuario;
import com.gestion.erp.shared.models.BaseEntity;

@Entity
@Table(name = "equipos")
@Getter @Setter
@NoArgsConstructor
public class Equipo extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique = true, nullable = false)
    private String nombre;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "supervisor_id")
    private Usuario supervisor;

    @OneToMany(mappedBy = "equipo")
    private List<Conductor> conductores = new ArrayList<>();
}
