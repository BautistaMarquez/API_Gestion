package com.gestion.erp.modules.logistica.services;

import java.time.LocalDateTime;
import java.util.ArrayList;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

// Imports de excepciones personalizadas
import com.gestion.erp.exception.BusinessException;
import com.gestion.erp.exception.EntityNotFoundException;
import com.gestion.erp.exception.ResourceConflictException;

// Imports de módulos
import com.gestion.erp.modules.logistica.dtos.*;
import com.gestion.erp.modules.logistica.models.*;
import com.gestion.erp.modules.logistica.models.enums.*;
import com.gestion.erp.modules.logistica.repositories.*;
import com.gestion.erp.modules.maestros.models.*;
import com.gestion.erp.modules.maestros.models.enums.*;
import com.gestion.erp.modules.maestros.repositories.*;

@Service
@RequiredArgsConstructor
public class ViajeService {

    private final ViajeRepository viajeRepository;
    private final VehiculoRepository vehiculoRepository;
    private final ConductorRepository conductorRepository;
    private final ProductoRepository productoRepository;
    private final ProductoPrecioRepository precioRepository;
    private final UsuarioRepository usuarioRepository; // Necesario para el supervisor

    @Transactional
    public Viaje registrarInicioViaje(ViajeRequestDTO request) {
        
        // 1. Validar y Obtener Vehículo (404 si no existe, 409 si está ocupado)
        Vehiculo vehiculo = vehiculoRepository.findById(request.vehiculoId())
            .orElseThrow(() -> new EntityNotFoundException("Vehículo con ID " + request.vehiculoId() + " no encontrado"));
        
        if (vehiculo.getEstado() != EstadoVehiculo.DISPONIBLE) {
            throw new ResourceConflictException("El vehículo " + vehiculo.getPatente() + " no está disponible (Estado actual: " + vehiculo.getEstado() + ")");
        }

        // 2. Validar y Obtener Conductor (404 si no existe, 409 si está ocupado)
        Conductor conductor = conductorRepository.findById(request.conductorId())
            .orElseThrow(() -> new EntityNotFoundException("Conductor con ID " + request.conductorId() + " no encontrado"));

        if (conductor.getEstado() != EstadoConductor.DISPONIBLE) {
            throw new ResourceConflictException("El conductor " + conductor.getNombre() + " ya está asignado a otro viaje");
        }

        // 3. Validar Supervisor (Regla de Auditoría)
        Usuario supervisor = usuarioRepository.findById(request.supervisorId())
            .orElseThrow(() -> new EntityNotFoundException("Supervisor con ID " + request.supervisorId() + " no encontrado"));

        // 4. Crear la cabecera del Viaje
        Viaje viaje = new Viaje();
        viaje.setFechaInicio(LocalDateTime.now());
        viaje.setEstado(EstadoViaje.EN_PROCESO);
        viaje.setVehiculo(vehiculo);
        viaje.setConductor(conductor);
        viaje.setSupervisor(supervisor);
        viaje.setDetalles(new ArrayList<>()); // Inicializar lista para evitar NullPointerException

        // 5. Procesar Detalles y Snapshot de Precios (Regla #6 y #3 de HUs)
        if (request.detalles() == null || request.detalles().isEmpty()) {
            throw new BusinessException("El viaje debe contener al menos un producto en la carga.");
        }

        for (DetalleRequestDTO detReq : request.detalles()) {
            // Validación cuantitativa
            if (detReq.cantidadInicial() <= 0) {
                throw new BusinessException("La cantidad inicial para el producto ID " + detReq.productoId() + " debe ser mayor a 0");
            }

            Producto producto = productoRepository.findById(detReq.productoId())
                .orElseThrow(() -> new EntityNotFoundException("Producto ID " + detReq.productoId() + " no existe"));

            // Obtenemos el precio específico y "congelamos" el valor
            ProductoPrecio precio = precioRepository.findById(detReq.productoPrecioId())
                .orElseThrow(() -> new EntityNotFoundException("El esquema de precio seleccionado no es válido"));

            ViajeDetalle detalle = new ViajeDetalle();
            detalle.setViaje(viaje);
            detalle.setProducto(producto);
            detalle.setCantidadInicial(detReq.cantidadInicial());
            detalle.setPrecioAplicado(precio.getValor()); // SNAPSHOT: Aquí se cumple la integridad contable

            viaje.getDetalles().add(detalle);
        }

        // 6. Actualizar estados de recursos (Side Effects)
        // Gracias a @Transactional, si algo falla arriba, esto no se aplica.
        vehiculo.setEstado(EstadoVehiculo.EN_VIAJE);
        conductor.setEstado(EstadoConductor.OCUPADO);

        // 7. Persistencia final
        return viajeRepository.save(viaje);
    }
}