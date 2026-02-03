package com.gestion.erp.modules.logistica.services;

import com.gestion.erp.modules.logistica.dtos.dashboard.*;
import com.gestion.erp.modules.logistica.repositories.DashboardRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class DashboardService {

    private final DashboardRepository dashboardRepo;

    public KpiStatsDTO getKpiStats(LocalDate from, LocalDate to, Long supervisorId) {
        return dashboardRepo.getKpis(
            from.atStartOfDay(), 
            to.atTime(23, 59, 59), 
            supervisorId
        );
    }

    public List<VentaDiariaDTO> getVentasTrend(LocalDate from, LocalDate to, Long supervisorId) {
        return dashboardRepo.getVentasTrend(
            from.atStartOfDay(), 
            to.atTime(23, 59, 59), 
            supervisorId
        );
    }

    public List<ProductoPerformanceDTO> getProductMix(LocalDate from, LocalDate to, Long supervisorId) {
        return dashboardRepo.getProductMix(
            from.atStartOfDay(), 
            to.atTime(23, 59, 59), 
            supervisorId
        );
    }

    public Page<DetalleAuditoriaDTO> getAuditoriaReport(LocalDate from, LocalDate to, Long supervisorId, Pageable pageable) {
        return dashboardRepo.getAuditoriaReport(
            from.atStartOfDay(), 
            to.atTime(23, 59, 59), 
            supervisorId, 
            pageable
        );
    }
}