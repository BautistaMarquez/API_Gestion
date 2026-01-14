package com.gestion.erp.modules.logistica.services;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.gestion.erp.exception.*;
import com.gestion.erp.modules.logistica.dtos.*;
import com.gestion.erp.modules.logistica.models.*;
import com.gestion.erp.modules.logistica.repositories.*;
import com.gestion.erp.modules.logistica.mappers.ViajeMapper; // Inyectamos el mapper
import com.gestion.erp.modules.maestros.models.*;
import com.gestion.erp.modules.maestros.models.enums.*;
import com.gestion.erp.modules.maestros.repositories.*;
import com.gestion.erp.modules.auth.models.Usuario;
import com.gestion.erp.modules.auth.repositories.UsuarioRepository;

@Service
@RequiredArgsConstructor
public class ViajeService {

    private final ViajeRepository viajeRepository;
    private final VehiculoRepository vehiculoRepository;
    private final ConductorRepository conductorRepository;
    private final ProductoRepository productoRepository;
    private final ProductoPrecioRepository precioRepository;
    private final UsuarioRepository usuarioRepository;
    private final ViajeMapper viajeMapper; // Nuestro nuevo aliado

    @Transactional
    public ViajeResponseDTO registrarInicioViaje(ViajeRequestDTO request) {
        
        // 1. ORQUESTACIÓN: Validar y obtener recursos (Estado y Existencia)
        Vehiculo vehiculo = validarYObtenerVehiculo(request.vehiculoId());
        Conductor conductor = validarYObtenerConductor(request.conductorId());
        Usuario supervisor = usuarioRepository.findById(request.supervisorId())
            .orElseThrow(() -> new EntityNotFoundException("Supervisor no encontrado"));

        // 2. MAPEO: Convertir DTO a Entidad (MapStruct maneja la estructura y la lista de detalles)
        Viaje viaje = viajeMapper.toEntity(request);

        // 3. VINCULACIÓN: Asignar objetos reales y lógica de snapshot
        viaje.setVehiculo(vehiculo);
        viaje.setConductor(conductor);
        viaje.setSupervisor(supervisor);

        // Procesar detalles para asegurar integridad contable (Precios y Productos)
        procesarDetallesYPrecios(viaje, request);

        // 4. EFECTOS SECUNDARIOS: Cambio de estados
        vehiculo.setEstado(EstadoVehiculo.EN_VIAJE);
        conductor.setEstado(EstadoConductor.OCUPADO);

        // 5. PERSISTENCIA Y RESPUESTA
        Viaje guardado = viajeRepository.save(viaje);
        return viajeMapper.toResponseDTO(guardado);
    }

    private void procesarDetallesYPrecios(Viaje viaje, ViajeRequestDTO request) {
        // MapStruct ya creó la lista de detalles, pero sin las referencias a Producto y Precios reales
        for (int i = 0; i < viaje.getDetalles().size(); i++) {
            ViajeDetalle detalle = viaje.getDetalles().get(i);
            DetalleRequestDTO detDto = request.detalles().get(i);

            // Validación de cantidad (Regla de negocio)
            if (detDto.cantidadInicial() <= 0) {
                throw new BusinessException("Cantidad inicial inválida para producto ID: " + detDto.productoId());
            }

            // Buscar entidades reales
            Producto producto = productoRepository.findById(detDto.productoId())
                .orElseThrow(() -> new EntityNotFoundException("Producto no existe"));
            
            ProductoPrecio precio = precioRepository.findById(detDto.productoPrecioId())
                .orElseThrow(() -> new EntityNotFoundException("Esquema de precio no válido"));

            // Completar el detalle con datos del sistema
            detalle.setViaje(viaje);
            detalle.setProducto(producto);
            detalle.setPrecioAplicado(precio.getValor()); // SNAPSHOT CONTABLE
        }
    }

    private Vehiculo validarYObtenerVehiculo(Long id) {
        Vehiculo v = vehiculoRepository.findById(id)
            .orElseThrow(() -> new EntityNotFoundException("Vehículo no encontrado"));
        if (v.getEstado() != EstadoVehiculo.DISPONIBLE) {
            throw new ResourceConflictException("El vehículo " + v.getPatente() + " está " + v.getEstado());
        }
        return v;
    }

    private Conductor validarYObtenerConductor(Long id) {
        Conductor c = conductorRepository.findById(id)
            .orElseThrow(() -> new EntityNotFoundException("Conductor no encontrado"));
        if (c.getEstado() != EstadoConductor.DISPONIBLE) {
            throw new ResourceConflictException("El conductor " + c.getNombre() + " no está disponible");
        }
        return c;
    }
}