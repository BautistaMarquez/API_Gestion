package com.gestion.erp.modules.maestros.repositories;

import com.gestion.erp.modules.maestros.models.Vehiculo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface VehiculoRepository extends JpaRepository<Vehiculo, Long> {
    Optional<Vehiculo> findByPatente(String patente);
    boolean existsByPatenteIgnoreCase(String patente);
}