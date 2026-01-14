-- =============================================================================
-- 1. ACTUALIZACIÓN DE TABLAS EXISTENTES (AUDITORÍA + OPTIMISTIC LOCKING)
-- =============================================================================

-- Agregamos campos de auditoría y versión a las tablas de V1 y V2
ALTER TABLE usuarios 
    ADD COLUMN created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    ADD COLUMN updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    ADD COLUMN created_by VARCHAR(255);

ALTER TABLE productos 
    ADD COLUMN created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    ADD COLUMN updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    ADD COLUMN created_by VARCHAR(255);

ALTER TABLE vehiculos 
    ADD COLUMN version BIGINT DEFAULT 0, -- Necesario para @Version
    ADD COLUMN created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    ADD COLUMN updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    ADD COLUMN created_by VARCHAR(255);

ALTER TABLE conductores 
    ADD COLUMN version BIGINT DEFAULT 0, -- Necesario para @Version
    ADD COLUMN created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    ADD COLUMN updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    ADD COLUMN created_by VARCHAR(255);

ALTER TABLE viajes
    ADD COLUMN version BIGINT DEFAULT 0, -- Necesario para @Version
    ADD COLUMN created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    ADD COLUMN updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    ADD COLUMN created_by VARCHAR(255);

ALTER TABLE viaje_detalles
    ADD COLUMN created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    ADD COLUMN updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    ADD COLUMN created_by VARCHAR(255);


-- =============================================================================
-- 2. CREACIÓN DE NUEVAS TABLAS SEGÚN EL DER
-- =============================================================================

-- Tabla de Equipos
CREATE TABLE equipos (
    id BIGSERIAL PRIMARY KEY,
    nombre VARCHAR(100) UNIQUE NOT NULL,
    supervisor_id BIGINT REFERENCES usuarios(id),
    -- Auditoría
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    created_by VARCHAR(255)
);

-- Tabla de Precios (Tarifario)
CREATE TABLE producto_precios (
    id BIGSERIAL PRIMARY KEY,
    producto_id BIGINT NOT NULL REFERENCES productos(id),
    etiqueta VARCHAR(50) NOT NULL, -- 'Minorista', 'Mayorista', etc.
    valor NUMERIC(12, 2) NOT NULL CHECK (valor >= 0),
    -- Auditoría
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    created_by VARCHAR(255)
);

-- =============================================================================
-- 3. VÍNCULOS FALTANTES
-- =============================================================================

-- Relacionamos conductores con equipos
ALTER TABLE conductores 
    ADD COLUMN equipo_id BIGINT;

ALTER TABLE conductores 
    ADD CONSTRAINT fk_conductor_equipo 
    FOREIGN KEY (equipo_id) REFERENCES equipos(id);