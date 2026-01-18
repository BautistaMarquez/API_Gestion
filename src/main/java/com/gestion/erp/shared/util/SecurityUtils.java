package com.gestion.erp.shared.util;

import com.gestion.erp.modules.auth.models.Usuario;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

@Component
public class SecurityUtils {

    public Usuario getCurrentUser() {
        return (Usuario) SecurityContextHolder.getContext()
                .getAuthentication()
                .getPrincipal();
    }

    public Long getCurrentUserId() {
        return getCurrentUser().getId();
    }
}