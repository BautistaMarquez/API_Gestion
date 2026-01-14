package com.gestion.erp.modules.maestros.repositories;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import com.gestion.erp.modules.maestros.models.Equipo;

@Repository
public interface EquipoRepository extends JpaRepository<Equipo, Long> {
    
}
