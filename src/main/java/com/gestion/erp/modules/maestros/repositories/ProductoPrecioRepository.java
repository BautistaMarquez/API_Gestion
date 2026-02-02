package com.gestion.erp.modules.maestros.repositories;
import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.gestion.erp.modules.maestros.models.ProductoPrecio;

@Repository
public interface ProductoPrecioRepository extends JpaRepository<ProductoPrecio, Long> {
    Optional<ProductoPrecio> findByProductoId(Long productoId);
    List<ProductoPrecio> findAllByProductoId(Long productoId);
}
