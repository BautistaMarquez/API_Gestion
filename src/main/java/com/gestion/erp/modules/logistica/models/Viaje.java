package com.gestion.erp.modules.logistica.models;

import java.time.LocalDateTime;
import jakarta.persistence.*;
import lombok.*;
import java.util.List;

import com.gestion.erp.modules.maestros.models.Conductor;
import com.gestion.erp.modules.maestros.models.Vehiculo;
import com.gestion.erp.modules.maestros.models.enums.EstadoConductor;
import com.gestion.erp.modules.maestros.models.enums.EstadoVehiculo;
import com.gestion.erp.shared.models.BaseEntity;
import com.gestion.erp.exception.BusinessException;
import com.gestion.erp.modules.auth.models.Usuario;
import com.gestion.erp.modules.logistica.models.enums.EstadoViaje;
import java.math.BigDecimal;

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

    @Column(name = "venta_total")
    @Builder.Default
    private BigDecimal ventaTotal = BigDecimal.ZERO;

    @OneToMany(mappedBy = "viaje", cascade = CascadeType.ALL)
    @Builder.Default
    private List<ViajeDetalle> detalles = new ArrayList<>();
    
    @Version
    private Long version;

    public ViajeDetalle obtenerDetalle(Long detalleId) {
    return this.detalles.stream()
        .filter(d -> d.getId().equals(detalleId))
        .findFirst()
        .orElseThrow(() -> new BusinessException("El detalle solicitado no pertenece a este viaje"));
    }

    public void finalizar() {
    this.fechaFin = LocalDateTime.now();
    this.estado = EstadoViaje.FINALIZADO;
    // Liberamos recursos
    this.vehiculo.setEstado(EstadoVehiculo.DISPONIBLE);
    this.conductor.setEstado(EstadoConductor.DISPONIBLE);
    }
}
