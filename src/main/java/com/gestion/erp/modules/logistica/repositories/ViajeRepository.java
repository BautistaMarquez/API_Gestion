package com.gestion.erp.modules.logistica.repositories;

import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.gestion.erp.modules.logistica.dtos.DashboardDTO;
import com.gestion.erp.modules.logistica.models.Viaje;
import java.util.List;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

@Repository
public interface ViajeRepository extends JpaRepository<Viaje, Long>, JpaSpecificationExecutor<Viaje> {

    @EntityGraph(attributePaths = {"supervisor", "vehiculo", "conductor"})
    List<Viaje> findBySupervisorId(Long supervisorId);

    @EntityGraph(attributePaths = {"supervisor", "vehiculo", "conductor", "detalles"})
    List<Viaje> findWithDetailsBySupervisorId(Long supervisorId);

    // Query para el ADMIN (Global)
    @Query("SELECT new com.gestion.erp.modules.logistica.dtos.DashboardDTO(" +
           "COUNT(v), " + // Devuelve Long -> DashboardDTO.totalViajes
           "SUM(CASE WHEN v.estado = 'EN_PROCESO' THEN 1L ELSE 0L END), " + // 1L/0L asegura Long -> DashboardDTO.viajesEnProceso
           "SUM(CASE WHEN v.estado = 'FINALIZADO' THEN 1L ELSE 0L END), " + // 1L/0L asegura Long -> DashboardDTO.viajesFinalizados
           "COALESCE(SUM(v.ventaTotal), 0), " + // COALESCE con 0 para BigDecimal -> DashboardDTO.ventaTotalEfectiva
           "COALESCE(AVG(v.ventaTotal), 0.0)) " + // AVG siempre devuelve Double -> DashboardDTO.promedioVentaPorViaje
           "FROM Viaje v")
    DashboardDTO getGlobalStats();

    // Query para el SUPERVISOR (Filtrado)
    @Query("SELECT new com.gestion.erp.modules.logistica.dtos.DashboardDTO(" +
           "COUNT(v), " +
           "SUM(CASE WHEN v.estado = 'EN_PROCESO' THEN 1L ELSE 0L END), " +
           "SUM(CASE WHEN v.estado = 'FINALIZADO' THEN 1L ELSE 0L END), " +
           "COALESCE(SUM(v.ventaTotal), 0), " +
           "COALESCE(AVG(v.ventaTotal), 0.0)) " +
           "FROM Viaje v WHERE v.supervisor.id = :supervisorId")
    DashboardDTO getStatsBySupervisor(Long supervisorId);
}