CREATE TABLE viajes (
    id BIGSERIAL PRIMARY KEY,
    fecha_inicio TIMESTAMP NOT NULL,
    fecha_fin TIMESTAMP,
    estado VARCHAR(20) NOT NULL, -- EN_PROCESO, FINALIZADO
    supervisor_id BIGINT REFERENCES usuarios(id),
    vehiculo_id BIGINT REFERENCES vehiculos(id),
    conductor_id BIGINT REFERENCES conductores(id),
    venta_total DECIMAL(12, 2) DEFAULT 0.00
);

CREATE TABLE viaje_detalles (
    id BIGSERIAL PRIMARY KEY,
    viaje_id BIGINT REFERENCES viajes(id),
    producto_id BIGINT REFERENCES productos(id),
    cantidad_inicial INTEGER NOT NULL CHECK (cantidad_inicial > 0),
    cantidad_final INTEGER CHECK (cantidad_final >= 0),
    venta_realizada DECIMAL(12, 2) DEFAULT 0.00,
    precio_aplicado DECIMAL(12, 2) NOT NULL -- Aquí "congelamos" el precio del momento
);