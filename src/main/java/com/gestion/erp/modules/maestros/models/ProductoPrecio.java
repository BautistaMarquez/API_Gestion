package com.gestion.erp.modules.maestros.models;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import java.math.BigDecimal;
import com.gestion.erp.shared.models.BaseEntity;

@Entity
@Table(name = "producto_precios")
@Getter @Setter
@NoArgsConstructor
public class ProductoPrecio extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "producto_id", nullable = false)
    private Producto producto;

    private String etiqueta; // "Mayorista", "Minorista", "Especial"

    @Column(nullable = false)
    private BigDecimal valor;
}