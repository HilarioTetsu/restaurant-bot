-- =============================================================================
-- 1. INICIALIZACIÓN DEL RESTAURANTE: TACOS CENTRO
-- =============================================================================
CREATE SCHEMA IF NOT EXISTS tacos_centro;

CREATE TABLE tacos_centro.platillos (
    id BIGSERIAL PRIMARY KEY,
    nombre VARCHAR(100) NOT NULL,
    precio NUMERIC(10, 2) NOT NULL,
    disponible BOOLEAN DEFAULT TRUE
);

CREATE TABLE tacos_centro.pedidos (
    id VARCHAR(36) PRIMARY KEY, -- Almacenará el UUID de la sesión del bot
    telefono_cliente VARCHAR(20) NOT NULL,
    estado VARCHAR(50) NOT NULL, -- EN_CREACION, PENDIENTE_VALIDACION, EN_COCINA, LISTO
    total NUMERIC(10, 2) DEFAULT 0.00,
    fecha_creacion TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

-- Datos de prueba para Tacos Centro
INSERT INTO tacos_centro.platillos (nombre, precio) VALUES 
('Taco de Pastor', 18.00),
('Taco de Bistec', 20.00),
('Gringa de Pastor', 45.00),
('Agua de Horchata Grande', 25.00);


-- =============================================================================
-- 2. INICIALIZACIÓN DEL RESTAURANTE: LAS BRISAS
-- =============================================================================
CREATE SCHEMA IF NOT EXISTS las_brisas;

CREATE TABLE las_brisas.platillos (
    id BIGSERIAL PRIMARY KEY,
    nombre VARCHAR(100) NOT NULL,
    precio NUMERIC(10, 2) NOT NULL,
    disponible BOOLEAN DEFAULT TRUE
);

CREATE TABLE las_brisas.pedidos (
    id VARCHAR(36) PRIMARY KEY,
    telefono_cliente VARCHAR(20) NOT NULL,
    estado VARCHAR(50) NOT NULL,
    total NUMERIC(10, 2) DEFAULT 0.00,
    fecha_creacion TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

-- Datos de prueba para Las Brisas (Menú completamente distinto)
INSERT INTO las_brisas.platillos (nombre, precio) VALUES 
('Hamburguesa Con Queso', 85.00),
('Papas a la Francesa', 35.00),
('Boneless BBQ', 110.00),
('Refresco de Lata', 22.00);