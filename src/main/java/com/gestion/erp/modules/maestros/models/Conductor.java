package com.gestion.erp.modules.maestros.models;

import java.time.LocalDate;

import com.gestion.erp.modules.maestros.models.enums.EstadoConductor;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.*;

@Entity
@Table(name = "conductores")
@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
public class Conductor {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank(message = "El DNI es obligatorio")
    @Column(unique = true, nullable = false, length = 15)
    private String dni;

    @NotBlank(message = "El nombre es obligatorio")
    private String nombre;

    @NotNull(message = "La fecha de vencimiento de licencia es obligatoria")
    @Column(name = "licencia_vencimiento")
    private LocalDate licenciaVencimiento;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    @Builder.Default
    private EstadoConductor estado = EstadoConductor.DISPONIBLE;
}
