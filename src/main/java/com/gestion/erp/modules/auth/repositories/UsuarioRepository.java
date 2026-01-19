package com.gestion.erp.modules.auth.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import com.gestion.erp.modules.auth.models.Usuario;
import java.util.Optional;
import java.util.List;
import com.gestion.erp.modules.auth.models.enums.RolUsuario;

@Repository
public interface UsuarioRepository extends JpaRepository<Usuario, Long> {
    boolean existsByMailIgnoreCase(String mail);
    Optional<Usuario> findByMailIgnoreCase(String mail);
    Optional<Usuario> findByMailIgnoreCaseAndActivoTrue(String mail);
    List<Usuario> findByRolAndActivoTrue(RolUsuario rol);
}
