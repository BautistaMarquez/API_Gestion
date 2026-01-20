package com.gestion.erp.modules.logistica.Specification;
import com.gestion.erp.modules.logistica.models.Viaje;
import com.gestion.erp.modules.logistica.models.enums.EstadoViaje;
import org.springframework.data.jpa.domain.Specification;
import java.time.LocalDateTime;

public class ViajeSpecification {

    public static Specification<Viaje> conFechaEntre(LocalDateTime inicio, LocalDateTime fin) {
        return (root, query, cb) -> {
            if (inicio == null || fin == null) return null;
            return cb.between(root.get("fechaInicio"), inicio, fin);
        };
    }

    public static Specification<Viaje> conEstado(EstadoViaje estado) {
        return (root, query, cb) -> {
            if (estado == null) return null;
            return cb.equal(root.get("estado"), estado);
        };
    }

    public static Specification<Viaje> conPatente(String patente) {
        return (root, query, cb) -> {
            if (patente == null || patente.isEmpty()) return null;
            // Join con la tabla Vehiculo
            return cb.like(cb.lower(root.get("vehiculo").get("patente")), "%" + patente.toLowerCase() + "%");
        };
    }
}
