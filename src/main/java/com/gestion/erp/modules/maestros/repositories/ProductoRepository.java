package com.gestion.erp.modules.maestros.repositories;

import com.gestion.erp.modules.maestros.models.Producto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ProductoRepository extends JpaRepository<Producto, Long> {
    // Buscador útil para validar nombres duplicados antes de insertar
    Optional<Producto> findByNombre(String nombre);
    boolean existsByNombreIgnoreCase(String nombre);
    
    // Query nativa para traer TODOS los productos (activos e inactivos)
    // Ignora el @SQLRestriction para uso en gestión administrativa
    @Query(value = "SELECT * FROM productos ORDER BY nombre", nativeQuery = true)
    List<Producto> findAllIncludingInactive();
    
    @Query(value = "SELECT * FROM productos ORDER BY nombre", 
           countQuery = "SELECT count(*) FROM productos",
           nativeQuery = true)
    Page<Producto> findAllIncludingInactive(Pageable pageable);
    
    // Buscar producto por ID ignorando el filtro de activo (para poder reactivar productos)
    @Query(value = "SELECT * FROM productos WHERE id = :id", nativeQuery = true)
    Optional<Producto> findByIdIncludingInactive(@Param("id") Long id);
}