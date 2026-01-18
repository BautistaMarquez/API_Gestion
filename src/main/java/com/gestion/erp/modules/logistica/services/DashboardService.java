package com.gestion.erp.modules.logistica.services;
import com.gestion.erp.modules.auth.models.Usuario;
import com.gestion.erp.modules.logistica.dtos.DashboardDTO;
import com.gestion.erp.modules.logistica.repositories.ViajeRepository;
import com.gestion.erp.shared.util.SecurityUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.stereotype.Service;


@Service
@RequiredArgsConstructor
public class DashboardService {
    private final ViajeRepository repository;
    private final SecurityUtils securityUtils;

    public DashboardDTO obtenerEstadisticas() {
        Usuario usuario = securityUtils.getCurrentUser();
        
        // Si es ADMIN o TOTAL, ve la foto completa de la empresa
        if (usuario.getRol().name().equals("ADMIN") || usuario.getRol().name().equals("TOTAL")) {
            return repository.getGlobalStats();
        }
        
        // Si es SUPERVISOR, ve solo el rendimiento de su equipo
        if (usuario.getRol().name().equals("SUPERVISOR")) {
            return repository.getStatsBySupervisor(usuario.getId());
        }

        throw new AccessDeniedException("Tu rol no tiene acceso a estadísticas");
    }
}
