package com.gestion.erp.modules.auth.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
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

    @Query("SELECT u FROM Usuario u WHERE u.rol = :rol AND u.id NOT IN (SELECT e.supervisor.id FROM Equipo e WHERE e.supervisor IS NOT NULL)")
    List<Usuario> findSupervisoresDisponibles(@Param("rol") RolUsuario rol);
}
