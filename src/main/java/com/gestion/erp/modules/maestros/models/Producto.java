package com.gestion.erp.modules.maestros.models;

import jakarta.persistence.*;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.*;

import java.math.BigDecimal;

import com.gestion.erp.shared.models.BaseEntity;

@Entity
@Table(name = "productos")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Producto extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank(message = "El nombre del producto es mandatorio")
    @Column(unique = true, nullable = false)
    private String nombre;

    @NotNull(message = "El precio unitario no puede ser nulo")
    @DecimalMin(value = "0.01", message = "El precio debe ser mayor a cero")
    @Column(name = "precio_unitario", nullable = false, precision = 12, scale = 2)
    private BigDecimal precioUnitario;
}
