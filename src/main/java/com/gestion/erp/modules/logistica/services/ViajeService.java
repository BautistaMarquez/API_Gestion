package com.gestion.erp.modules.logistica.services;

import lombok.RequiredArgsConstructor;

import java.math.BigDecimal;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import com.gestion.erp.exception.*;
import com.gestion.erp.modules.auth.models.Usuario;
import com.gestion.erp.modules.logistica.dtos.*;
import com.gestion.erp.modules.logistica.models.*;
import com.gestion.erp.modules.logistica.models.enums.EstadoViaje;
import com.gestion.erp.modules.logistica.repositories.*;
import com.gestion.erp.modules.logistica.mappers.ViajeMapper; // Inyectamos el mapper
import com.gestion.erp.modules.maestros.models.*;
import com.gestion.erp.modules.maestros.models.enums.*;
import com.gestion.erp.modules.maestros.repositories.*;
import com.gestion.erp.modules.maestros.services.ConductorService;
import com.gestion.erp.modules.maestros.services.VehiculoService;
import com.gestion.erp.shared.util.SecurityUtils;
import java.util.List;


@Service
@RequiredArgsConstructor
public class ViajeService {
    // Servicios externos (Inyección de dependencias)
    private final VehiculoService vehiculoService;
    private final ConductorService conductorService;
    private final ProductoRepository productoRepository;
    private final ProductoPrecioRepository precioRepository;
    private final SecurityUtils securityUtils;
    
    // Repositorios y Mappers
    private final ViajeRepository viajeRepository;
    private final ViajeMapper viajeMapper;

    public List<ViajeResponseDTO> listarViajes() {
        Usuario usuarioActual = securityUtils.getCurrentUser();
        
        // Si es SUPERVISOR, filtramos por su ID.
        if (usuarioActual.getRol().name().equals("SUPERVISOR")) {
            return viajeRepository.findBySupervisorId(usuarioActual.getId())
                             .stream().map(viajeMapper::toResponseDTO).toList();
        }
        // Sino, ve todo.
        return viajeRepository.findAll().stream().map(viajeMapper::toResponseDTO).toList();
    }

    @Transactional
    public ViajeResponseDTO registrarInicioViaje(ViajeRequestDTO request) {
        // 1. Obtener recursos validados de otros servicios
        Vehiculo vehiculo = vehiculoService.obtenerParaViaje(request.vehiculoId());
        Conductor conductor = conductorService.validarYObtenerParaViaje(request.conductorId());
        
        // 2. Mapear y vincular
        Viaje viaje = viajeMapper.toEntity(request);
        viaje.setVehiculo(vehiculo);
        viaje.setConductor(conductor);
        
        // 3. Snapshot de precios (Lógica de orquestación)
        procesarDetallesYPrecios(viaje, request);

        // 4. Cambio de estados
        vehiculo.setEstado(EstadoVehiculo.EN_VIAJE);
        conductor.setEstado(EstadoConductor.OCUPADO);

        return viajeMapper.toResponseDTO(viajeRepository.save(viaje));
    }

    @Transactional
    public ViajeResponseDTO finalizarViaje(ViajeCierreRequestDTO request) {
        // 1. Buscar el viaje (Método de soporte abajo)
        Viaje viaje = buscarViajeEnProceso(request.viajeId());
        
        // 2. Validación de Conciliación (Asegurar que informan todos los productos)
        if (request.detallesFinales().size() != viaje.getDetalles().size()) {
            throw new BusinessException("Debe informar el retorno de todos los productos cargados (" 
                + viaje.getDetalles().size() + " ítems).");
        }

        // 3. Liquidación (Delegando a la lógica de dominio)
        BigDecimal ventaTotalAcumulada = BigDecimal.ZERO;
        
        for (DetalleCierreDTO dto : request.detallesFinales()) {
            ViajeDetalle detalle = viaje.obtenerDetalle(dto.detalleId());
            detalle.liquidar(dto.cantidadFinal()); // La entidad calcula su propia venta
            ventaTotalAcumulada = ventaTotalAcumulada.add(detalle.getVentaRealizada());
        }

        // 4. Cierre administrativo y liberación de recursos
        viaje.setVentaTotal(ventaTotalAcumulada);
        viaje.finalizar(); 

        return viajeMapper.toResponseDTO(viajeRepository.save(viaje));
    }

    // --- MÉTODOS DE SOPORTE (Private Helpers) ---

    private Viaje buscarViajeEnProceso(Long id) {
        Viaje viaje = viajeRepository.findById(id)
            .orElseThrow(() -> new EntityNotFoundException("Viaje no encontrado"));
        
        if (viaje.getEstado() != EstadoViaje.EN_PROCESO) {
            throw new ResourceConflictException("El viaje ya se encuentra " + viaje.getEstado());
        }
        return viaje;
    }

    private void procesarDetallesYPrecios(Viaje viaje, ViajeRequestDTO request) {
        for (int i = 0; i < viaje.getDetalles().size(); i++) {
            ViajeDetalle detalle = viaje.getDetalles().get(i);
            DetalleRequestDTO detDto = request.detalles().get(i);

            Producto producto = productoRepository.findById(detDto.productoId())
                .orElseThrow(() -> new EntityNotFoundException("Producto no existe"));
            
            ProductoPrecio precio = precioRepository.findById(detDto.productoPrecioId())
                .orElseThrow(() -> new EntityNotFoundException("Esquema de precio no válido"));

            detalle.setViaje(viaje);
            detalle.setProducto(producto);
            detalle.setPrecioAplicado(precio.getValor());
        }
    }
}