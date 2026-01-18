package com.gestion.erp.modules.auth.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import com.gestion.erp.modules.auth.models.Usuario;

@Repository
public interface UsuarioRepository extends JpaRepository<Usuario, Long> {
    boolean existsByMailIgnoreCase(String mail);
    java.util.Optional<Usuario> findByMailIgnoreCase(String mail);
}
