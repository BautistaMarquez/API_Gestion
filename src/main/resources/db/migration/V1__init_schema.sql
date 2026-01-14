-- 1. Tabla de Usuarios (Seguridad RBAC)
CREATE TABLE usuarios (
    id BIGSERIAL PRIMARY KEY,
    mail VARCHAR(50) UNIQUE NOT NULL,
    password VARCHAR(255) NOT NULL,
    nombre VARCHAR(50) NOT NULL,
    apellido VARCHAR(100) NOT NULL,
    rol VARCHAR(20) NOT NULL, -- ADMIN, SUPERVISOR, CHOFER
    activo BOOLEAN DEFAULT TRUE
);

-- 2. Tabla de Productos (Catálogo)
CREATE TABLE productos (
    id BIGSERIAL PRIMARY KEY,
    nombre VARCHAR(100) UNIQUE NOT NULL, -- Regla #1: Nombre único
    activo BOOLEAN NOT NULL DEFAULT TRUE -- Regla #2: Producto activo/inactivo
);

-- 3. Tabla de Vehículos (Logística)
CREATE TABLE vehiculos (
    id BIGSERIAL PRIMARY KEY,
    patente VARCHAR(10) UNIQUE NOT NULL, -- Regla #3: Patente única
    modelo VARCHAR(100),
    estado VARCHAR(20) NOT NULL DEFAULT 'DISPONIBLE' -- DISPONIBLE, EN_VIAJE, MANTENIMIENTO
);

-- 4. Tabla de Conductores
CREATE TABLE conductores (
    id BIGSERIAL PRIMARY KEY,
    dni VARCHAR(15) UNIQUE NOT NULL,
    nombre VARCHAR(50) NOT NULL,
    apellido VARCHAR(100) NOT NULL,
    licencia_vencimiento DATE NOT NULL,
    estado VARCHAR(20) NOT NULL DEFAULT 'DISPONIBLE'
);