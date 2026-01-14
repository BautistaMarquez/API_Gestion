package com.gestion.erp.modules.maestros.models;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import lombok.*;

import java.util.ArrayList;
import java.util.List;

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

    // Relación Bidireccional: Un producto tiene muchos precios (Tarifario)
    // mappedBy: indica que el campo "producto" en la clase ProductoPrecio es el dueño de la relación
    // cascade: si guardas un producto con precios nuevos, se guardan automáticamente
    @OneToMany(mappedBy = "producto", cascade = CascadeType.ALL, orphanRemoval = true)
    @Builder.Default
    private List<ProductoPrecio> precios = new ArrayList<>();

    // Helper Method
    // Facilita la sincronización de ambos lados de la relación
    public void addPrecio(ProductoPrecio precio) {
        precios.add(precio);
        precio.setProducto(this);
    }
}
