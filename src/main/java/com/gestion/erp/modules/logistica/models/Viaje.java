package com.gestion.erp.modules.logistica.models;

import java.time.LocalDateTime;
import jakarta.persistence.*;
import lombok.*;
import java.util.List;

import com.gestion.erp.modules.maestros.models.Conductor;
import com.gestion.erp.modules.maestros.models.Vehiculo;
import com.gestion.erp.shared.models.BaseEntity;
import com.gestion.erp.modules.auth.models.Usuario;
import com.gestion.erp.modules.logistica.models.enums.EstadoViaje;

import java.util.ArrayList;



@Entity
@Table(name = "viajes")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Viaje extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "fecha_inicio", nullable = false)
    private LocalDateTime fechaInicio;

    @Column(name = "fecha_fin")
    private LocalDateTime fechaFin;

    @Enumerated(EnumType.STRING)
    private EstadoViaje estado; // Enum: EN_PROCESO, FINALIZADO

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "supervisor_id")
    private Usuario supervisor;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "vehiculo_id")
    private Vehiculo vehiculo;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "conductor_id")
    private Conductor conductor;

    @OneToMany(mappedBy = "viaje", cascade = CascadeType.ALL)
    @Builder.Default
    private List<ViajeDetalle> detalles = new ArrayList<>();
    
}
