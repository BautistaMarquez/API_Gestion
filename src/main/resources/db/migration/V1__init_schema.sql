-- 1. Tabla de Usuarios (Seguridad RBAC)
CREATE TABLE usuarios (
    id BIGSERIAL PRIMARY KEY,
    username VARCHAR(50) UNIQUE NOT NULL,
    password VARCHAR(255) NOT NULL,
    nombre_completo VARCHAR(150) NOT NULL,
    rol VARCHAR(20) NOT NULL, -- ADMIN, SUPERVISOR, CHOFER
    activo BOOLEAN DEFAULT TRUE
);

-- 2. Tabla de Productos (Catálogo)
CREATE TABLE productos (
    id BIGSERIAL PRIMARY KEY,
    nombre VARCHAR(100) UNIQUE NOT NULL, -- Regla #1: Nombre único
    precio_unitario DECIMAL(12, 2) NOT NULL CHECK (precio_unitario >= 0)
);

-- 3. Tabla de Vehículos (Logística)
CREATE TABLE vehiculos (
    id BIGSERIAL PRIMARY KEY,
    patente VARCHAR(10) UNIQUE NOT NULL, -- Regla #3: Patente única
    modelo VARCHAR(100),
    estado VARCHAR(20) NOT NULL DEFAULT 'DISPONIBLE' -- DISPONIBLE, EN_VIAJE, MANTENIMIENTO
);

-- 4. Tabla de Choferes
CREATE TABLE choferes (
    id BIGSERIAL PRIMARY KEY,
    dni VARCHAR(15) UNIQUE NOT NULL,
    nombre VARCHAR(150) NOT NULL,
    licencia_vencimiento DATE NOT NULL,
    estado VARCHAR(20) NOT NULL DEFAULT 'DISPONIBLE'
);