package com.gestion.erp.modules.maestros.repositories;

import com.gestion.erp.modules.maestros.models.Conductor;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ConductorRepository extends JpaRepository<Conductor, Long> {
    Optional<Conductor> findByDni(String dni);
    boolean existsByDni(String dni);
    List<Conductor> findByEquipoId(Long equipoId);
}