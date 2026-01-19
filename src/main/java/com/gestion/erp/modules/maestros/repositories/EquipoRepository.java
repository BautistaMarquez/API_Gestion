package com.gestion.erp.modules.maestros.repositories;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import com.gestion.erp.modules.maestros.models.Equipo;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

@Repository
public interface EquipoRepository extends JpaRepository<Equipo, Long> {
    boolean existsByNombreIgnoreCase(String nombre);
    Optional<Equipo> findBySupervisorId(Long supervisorId);

    @Query("SELECT e FROM Equipo e JOIN FETCH e.supervisor WHERE e.id = :id")
    Optional<Equipo> findByIdWithSupervisor(@Param("id") Long id);

    Page<Equipo> findAll(Pageable pageable);
}
