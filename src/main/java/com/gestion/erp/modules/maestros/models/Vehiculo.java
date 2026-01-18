package com.gestion.erp.modules.maestros.models;

import com.gestion.erp.modules.maestros.models.enums.EstadoVehiculo;
import com.gestion.erp.shared.models.BaseEntity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import lombok.*;
import org.hibernate.annotations.SQLRestriction;

@Entity
@Table(name = "vehiculos")
@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
@SQLRestriction("estado <> 'ELIMINADO'")
public class Vehiculo extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank(message = "La patente es obligatoria")
    @Column(unique = true, nullable = false, length = 10)
    private String patente;

    private String modelo;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    @Builder.Default
    private EstadoVehiculo estado = EstadoVehiculo.DISPONIBLE;

    @Version
    private Long version;
    
}