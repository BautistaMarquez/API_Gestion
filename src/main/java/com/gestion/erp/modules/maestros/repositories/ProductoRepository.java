package com.gestion.erp.modules.maestros.repositories;

import com.gestion.erp.modules.maestros.models.Producto;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ProductoRepository extends JpaRepository<Producto, Long> {
    // Buscador útil para validar nombres duplicados antes de insertar
    Optional<Producto> findByNombre(String nombre);
}