package com.gestion.erp.modules.logistica.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import com.gestion.erp.modules.logistica.models.Viaje;

@Repository
public interface ViajeRepository extends JpaRepository<Viaje, Long> {
    // Aquí podrías agregar métodos para buscar viajes por supervisor o estado en el futuro

}