package com.gestion.erp.modules.logistica.services;

import lombok.RequiredArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.gestion.erp.exception.*;
import com.gestion.erp.modules.logistica.dtos.*;
import com.gestion.erp.modules.logistica.models.*;
import com.gestion.erp.modules.logistica.models.enums.EstadoViaje;
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
    private final ViajeMapper viajeMapper;

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

    @Transactional
    public ViajeResponseDTO finalizarViaje(ViajeCierreRequestDTO request) {
        // 1. Validar existencia y estado del viaje
        Viaje viaje = viajeRepository.findById(request.viajeId())
            .orElseThrow(() -> new EntityNotFoundException("Viaje no encontrado"));

        if (viaje.getEstado() != EstadoViaje.EN_PROCESO) {
            throw new ResourceConflictException("Solo se pueden finalizar viajes que estén EN_PROCESO");
        }

        // 2. Procesar la liquidación de cada producto
        BigDecimal ventaTotalAcumulada = BigDecimal.ZERO;

        for (DetalleCierreDTO cierreDto : request.detallesFinales()) {
            // Buscamos el detalle específico dentro de la lista del viaje
            ViajeDetalle detalle = viaje.getDetalles().stream()
                .filter(d -> d.getId().equals(cierreDto.detalleId()))
                .findFirst()
                .orElseThrow(() -> new EntityNotFoundException("Detalle de viaje ID " + cierreDto.detalleId() + " no pertenece a este viaje"));

            // Validar integridad física: No puede volver más de lo que salió
            if (cierreDto.cantidadFinal() > detalle.getCantidadInicial()) {
                throw new BusinessException("Error Contable: La cantidad final (" + cierreDto.cantidadFinal() + 
                    ") no puede ser mayor a la inicial (" + detalle.getCantidadInicial() + ") para el producto " + detalle.getProducto().getNombre());
            }

            // --- LÓGICA CONTABLE ---
            int unidadesVendidas = detalle.getCantidadInicial() - cierreDto.cantidadFinal();
            BigDecimal ventaLinea = detalle.getPrecioAplicado().multiply(BigDecimal.valueOf(unidadesVendidas));

            // Actualizar el detalle
            detalle.setCantidadFinal(cierreDto.cantidadFinal());
            detalle.setVentaRealizada(ventaLinea);

            ventaTotalAcumulada = ventaTotalAcumulada.add(ventaLinea);
        }

        // 3. Actualizar cabecera del Viaje
        viaje.setFechaFin(LocalDateTime.now());
        viaje.setEstado(EstadoViaje.FINALIZADO);
        viaje.setVentaTotal(ventaTotalAcumulada);

        // 4. Liberar recursos (Importante para la disponibilidad del siguiente turno)
        viaje.getVehiculo().setEstado(EstadoVehiculo.DISPONIBLE);
        viaje.getConductor().setEstado(EstadoConductor.DISPONIBLE);

        // 5. Persistir y devolver respuesta aplanada
        Viaje guardado = viajeRepository.save(viaje);
        return viajeMapper.toResponseDTO(guardado);
    }
}