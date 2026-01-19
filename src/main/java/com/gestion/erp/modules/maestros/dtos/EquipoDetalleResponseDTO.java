package com.gestion.erp.modules.maestros.dtos;
import lombok.Data;
import java.util.List;
import com.gestion.erp.modules.auth.dtos.UsuarioResponseDTO;

@Data
public class EquipoDetalleResponseDTO {
    private Long id;
    private String nombre;
    
    // Información del Supervisor (desde tabla Usuario)
    private UsuarioResponseDTO supervisor; 
    
    // Lista de integrantes (desde tabla Conductor)
    private List<ConductorResponseDTO> conductores;
}