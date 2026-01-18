package com.gestion.erp.modules.maestros.repositories;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import com.gestion.erp.modules.maestros.models.Equipo;
import java.util.Optional;

@Repository
public interface EquipoRepository extends JpaRepository<Equipo, Long> {
    boolean existsByNombreIgnoreCase(String nombre);
    Optional<Equipo> findBySupervisorId(Long supervisorId);
}
