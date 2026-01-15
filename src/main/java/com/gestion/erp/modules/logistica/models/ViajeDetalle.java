package com.gestion.erp.modules.logistica.models;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import java.math.BigDecimal;
import com.gestion.erp.shared.models.BaseEntity;
import com.fasterxml.jackson.annotation.JsonBackReference;
import com.gestion.erp.exception.BusinessException;
import com.gestion.erp.modules.maestros.models.Producto;

@Entity
@Table(name = "viaje_detalles")
@Getter @Setter
@NoArgsConstructor
public class ViajeDetalle extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "viaje_id", nullable = false)
    @JsonBackReference
    private Viaje viaje;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "producto_id", nullable = false)
    private Producto producto;

    @Column(name = "cantidad_inicial", nullable = false)
    private Integer cantidadInicial;

    @Column(name = "cantidad_final")
    private Integer cantidadFinal;

    @Column(name = "venta_realizada")
    private BigDecimal ventaRealizada = BigDecimal.ZERO;

    @Column(name = "precio_aplicado", nullable = false)
    private BigDecimal precioAplicado; // El Snapshot del momento del viaje

    public void liquidar(Integer cantidadFinal) {
        if (cantidadFinal < 0 || cantidadFinal > this.cantidadInicial) {
            throw new BusinessException("Cantidad final inválida para " + this.producto.getNombre());
        }
        this.cantidadFinal = cantidadFinal;
        int unidadesVendidas = this.cantidadInicial - cantidadFinal;
        this.ventaRealizada = this.precioAplicado.multiply(BigDecimal.valueOf(unidadesVendidas));
    }
}
