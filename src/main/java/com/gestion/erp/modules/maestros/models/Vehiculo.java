package com.gestion.erp.modules.maestros.models;

import com.gestion.erp.modules.maestros.models.enums.EstadoVehiculo;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import lombok.*;

@Entity
@Table(name = "vehiculos")
@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
public class Vehiculo {

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
}