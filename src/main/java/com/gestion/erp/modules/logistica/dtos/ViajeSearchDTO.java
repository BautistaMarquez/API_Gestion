package com.gestion.erp.modules.logistica.dtos;

import com.gestion.erp.modules.logistica.models.enums.EstadoViaje;
import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDateTime;

@Data
public class ViajeSearchDTO {

    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)
    private LocalDateTime fechaDesde;

    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)
    private LocalDateTime fechaHasta;

    private EstadoViaje estado;

    private String patente;
    
    // En un futuro se pueden agregar más filtros si es necesario
}