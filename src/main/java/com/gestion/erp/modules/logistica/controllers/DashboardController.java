package com.gestion.erp.modules.logistica.controllers;

import com.gestion.erp.modules.auth.models.Usuario;
import com.gestion.erp.modules.auth.models.enums.RolUsuario;
import com.gestion.erp.modules.logistica.dtos.dashboard.*;
import com.gestion.erp.modules.logistica.services.DashboardService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("/api/v1/dashboard")
@RequiredArgsConstructor
@Tag(name = "Dashboard & BI", description = "Endpoints para gráficos y reportes gerenciales")
public class DashboardController {

    private final DashboardService dashboardService;

    @GetMapping("/stats")
    @Operation(summary = "Obtener KPI Cards (Totales generales)")
    public ResponseEntity<KpiStatsDTO> getStats(
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate from,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate to,
            Authentication auth) {
        return ResponseEntity.ok(dashboardService.getKpiStats(from, to, resolveSupervisorId(auth)));
    }

    @GetMapping("/trend")
    @Operation(summary = "Datos para gráfico de Evolución de Ventas")
    public ResponseEntity<List<VentaDiariaDTO>> getTrend(
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate from,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate to,
            Authentication auth) {
        return ResponseEntity.ok(dashboardService.getVentasTrend(from, to, resolveSupervisorId(auth)));
    }

    @GetMapping("/product-mix")
    @Operation(summary = "Datos para gráfico de Rendimiento de Productos")
    public ResponseEntity<List<ProductoPerformanceDTO>> getProductMix(
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate from,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate to,
            Authentication auth) {
        return ResponseEntity.ok(dashboardService.getProductMix(from, to, resolveSupervisorId(auth)));
    }

    @GetMapping("/audit-report")
    @Operation(summary = "Listado detallado paginado para auditoría")
    public ResponseEntity<Page<DetalleAuditoriaDTO>> getAuditReport(
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate from,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate to,
            @PageableDefault(size = 20) Pageable pageable, // Default 20 filas por página
            Authentication auth) {
        return ResponseEntity.ok(dashboardService.getAuditoriaReport(from, to, resolveSupervisorId(auth), pageable));
    }

    // --- Helper de Seguridad ---
    private Long resolveSupervisorId(Authentication auth) {
        Usuario usuario = (Usuario) auth.getPrincipal();
        // Si es ADMIN o TOTAL, retorna null (ver todo). Si no, retorna su ID.
        if (usuario.getRol() == RolUsuario.TOTAL || usuario.getRol() == RolUsuario.ADMIN) {
            return null;
        }
        return usuario.getId();
    }
}