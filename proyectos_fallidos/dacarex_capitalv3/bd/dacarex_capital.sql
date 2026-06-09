CREATE DATABASE IF NOT EXISTS dacarex_capital
    CHARACTER SET utf8mb4
    COLLATE utf8mb4_unicode_ci;

USE dacarex_capital;

CREATE TABLE IF NOT EXISTS usuarios (
    id          INT             AUTO_INCREMENT PRIMARY KEY,
    nombre      VARCHAR(100)    NOT NULL,
    email       VARCHAR(100)    NOT NULL UNIQUE,
    contrasenia VARCHAR(255)    NOT NULL,
    tipo_cuenta VARCHAR(20)     NOT NULL
);

CREATE TABLE IF NOT EXISTS categorias (
    id     INT          AUTO_INCREMENT PRIMARY KEY,
    nombre VARCHAR(100) NOT NULL,
    tipo   VARCHAR(20)  NOT NULL
);

CREATE TABLE IF NOT EXISTS movimientos (
    id           INT          AUTO_INCREMENT PRIMARY KEY,
    tipo         VARCHAR(20)  NOT NULL,
    descripcion  VARCHAR(200) NOT NULL,
    importe      DOUBLE       NOT NULL,
    categoria_id INT          NOT NULL,
    fecha        DATE         NOT NULL,
    notas        VARCHAR(500),
    FOREIGN KEY (categoria_id) REFERENCES categorias(id)
);

INSERT INTO categorias (nombre, tipo) VALUES
    ('Nómina',       'INGRESO'),
    ('Ventas',       'INGRESO'),
    ('Alquiler',     'GASTO'),
    ('Suministros',  'GASTO'),
    ('Marketing',    'GASTO'),
    ('Otros',        'GASTO');

INSERT INTO usuarios (nombre, email, contrasenia, tipo_cuenta) VALUES
    ('Demo', 'demo@dacarex.com', 'demo1234', 'PERSONAL');