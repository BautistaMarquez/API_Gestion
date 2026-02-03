package com.gestion.erp.modules.logistica.repositories;

import com.gestion.erp.modules.logistica.dtos.dashboard.*;
import com.gestion.erp.modules.logistica.models.Viaje;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface DashboardRepository extends JpaRepository<Viaje, Long> {

    // 1. KPI Stats
    // CORRECCIÓN: Cambiado v.supervisorId por v.supervisor.id
    // Nota: Efectividad = % promedio de carga vendida
    @Query("""
        SELECT new com.gestion.erp.modules.logistica.dtos.dashboard.KpiStatsDTO(
            COUNT(DISTINCT v.id),
            (SELECT COUNT(v2) FROM Viaje v2 WHERE v2.estado = 'EN_PROCESO' 
                AND (:supervisorId IS NULL OR v2.supervisor.id = :supervisorId)),
            COALESCE(SUM(v.ventaTotal), 0),
            COALESCE(AVG(
                CASE 
                    WHEN (SELECT SUM(d2.cantidadInicial) FROM ViajeDetalle d2 WHERE d2.viaje.id = v.id) > 0
                    THEN ((SELECT SUM(d2.cantidadInicial - d2.cantidadFinal) FROM ViajeDetalle d2 WHERE d2.viaje.id = v.id) * 100.0 / 
                          (SELECT SUM(d2.cantidadInicial) FROM ViajeDetalle d2 WHERE d2.viaje.id = v.id))
                    ELSE 0
                END
            ), 0.0)
        )
        FROM Viaje v
        WHERE v.estado = 'FINALIZADO'
        AND v.fechaFin BETWEEN :inicio AND :fin
        AND (:supervisorId IS NULL OR v.supervisor.id = :supervisorId)
    """)
    KpiStatsDTO getKpis(
        @Param("inicio") LocalDateTime inicio, 
        @Param("fin") LocalDateTime fin, 
        @Param("supervisorId") Long supervisorId
    );

    // 2. Tendencias
    // CORRECCIÓN: Cambiado v.supervisorId por v.supervisor.id
    @Query("""
        SELECT new com.gestion.erp.modules.logistica.dtos.dashboard.VentaDiariaDTO(
            CAST(v.fechaFin AS LocalDate),
            SUM(v.ventaTotal)
        )
        FROM Viaje v
        WHERE v.estado = 'FINALIZADO'
        AND v.fechaFin BETWEEN :inicio AND :fin
        AND (:supervisorId IS NULL OR v.supervisor.id = :supervisorId)
        GROUP BY CAST(v.fechaFin AS LocalDate)
        ORDER BY CAST(v.fechaFin AS LocalDate) ASC
    """)
    List<VentaDiariaDTO> getVentasTrend(
        @Param("inicio") LocalDateTime inicio, 
        @Param("fin") LocalDateTime fin, 
        @Param("supervisorId") Long supervisorId
    );

    // 3. Mix de Productos
    // CORRECCIÓN: Cambiado v.supervisorId por v.supervisor.id
    @Query("""
        SELECT new com.gestion.erp.modules.logistica.dtos.dashboard.ProductoPerformanceDTO(
            p.nombre,
            SUM(d.cantidadInicial - d.cantidadFinal),
            SUM(d.ventaRealizada)
        )
        FROM ViajeDetalle d
        JOIN d.viaje v
        JOIN d.producto p
        WHERE v.estado = 'FINALIZADO'
        AND v.fechaFin BETWEEN :inicio AND :fin
        AND (:supervisorId IS NULL OR v.supervisor.id = :supervisorId)
        GROUP BY p.nombre
        ORDER BY SUM(d.ventaRealizada) DESC
    """)
    List<ProductoPerformanceDTO> getProductMix(
        @Param("inicio") LocalDateTime inicio, 
        @Param("fin") LocalDateTime fin, 
        @Param("supervisorId") Long supervisorId
    );

    // 4. Auditoría
    // CORRECCIÓN: Cambiado v.supervisorId por v.supervisor.id
    // Incluye: Conductor, Vehículo (patente) y Equipo en lugar de ID y Supervisor
    @Query("""
        SELECT new com.gestion.erp.modules.logistica.dtos.dashboard.DetalleAuditoriaDTO(
            v.fechaFin,
            c.nombre || ' ' || c.apellido,
            veh.patente,
            COALESCE(e.nombre, 'Sin Equipo'),
            p.nombre,
            d.cantidadInicial,
            d.cantidadFinal,
            (d.cantidadInicial - d.cantidadFinal),
            d.precioAplicado,
            d.ventaRealizada
        )
        FROM ViajeDetalle d
        JOIN d.viaje v
        JOIN d.producto p
        JOIN v.conductor c
        JOIN v.vehiculo veh
        LEFT JOIN c.equipo e
        WHERE v.estado = 'FINALIZADO'
        AND v.fechaFin BETWEEN :inicio AND :fin
        AND (:supervisorId IS NULL OR v.supervisor.id = :supervisorId)
    """)
    Page<DetalleAuditoriaDTO> getAuditoriaReport(
        @Param("inicio") LocalDateTime inicio, 
        @Param("fin") LocalDateTime fin, 
        @Param("supervisorId") Long supervisorId,
        Pageable pageable
    );
}