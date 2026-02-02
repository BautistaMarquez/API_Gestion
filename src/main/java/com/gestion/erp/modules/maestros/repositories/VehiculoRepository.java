package com.gestion.erp.modules.maestros.repositories;

import com.gestion.erp.modules.maestros.models.Vehiculo;
import com.gestion.erp.modules.maestros.models.enums.EstadoVehiculo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface VehiculoRepository extends JpaRepository<Vehiculo, Long> {
    Optional<Vehiculo> findByPatente(String patente);
    boolean existsByPatenteIgnoreCase(String patente);
    List<Vehiculo> findByEstado(EstadoVehiculo estado);
    
    @Query(value = "SELECT * FROM vehiculos", countQuery = "SELECT count(*) FROM vehiculos", nativeQuery = true)
    Page<Vehiculo> findAllIncludingDeleted(Pageable pageable);
}