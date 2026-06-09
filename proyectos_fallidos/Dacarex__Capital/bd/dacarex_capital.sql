CREATE DATABASE IF NOT EXISTS dacarex_capital
    CHARACTER SET utf8mb4
    COLLATE utf8mb4_unicode_ci;

USE dacarex_capital;

CREATE TABLE IF NOT EXISTS usuarios (
    id              VARCHAR(50)     PRIMARY KEY,
    nombre_completo VARCHAR(100)    NOT NULL,
    email           VARCHAR(100)    NOT NULL UNIQUE,
    contrasenia     VARCHAR(255)    NOT NULL,
    tipo_cuenta     VARCHAR(20)     NOT NULL,
    nombre_empresa  VARCHAR(100),
    creado_en       DATETIME        NOT NULL,
    actualizado_en  DATETIME        NOT NULL
);

CREATE TABLE IF NOT EXISTS categorias (
    id              VARCHAR(50)     PRIMARY KEY,
    nombre          VARCHAR(100)    NOT NULL,
    tipo            VARCHAR(20)     NOT NULL,
    en_uso          BOOLEAN         NOT NULL DEFAULT false,
    creado_en       DATETIME        NOT NULL,
    actualizado_en  DATETIME        NOT NULL
);

CREATE TABLE IF NOT EXISTS movimientos (
    id                  VARCHAR(50)     PRIMARY KEY,
    tipo                VARCHAR(20)     NOT NULL,
    descripcion         VARCHAR(200)    NOT NULL,
    importe             DOUBLE          NOT NULL,
    categoria_id        VARCHAR(50)     NOT NULL,
    fecha               DATE            NOT NULL,
    notas               VARCHAR(500),
    saldo_resultante    DOUBLE          NOT NULL,
    creado_en           DATETIME        NOT NULL,
    actualizado_en      DATETIME        NOT NULL,
    FOREIGN KEY (categoria_id) REFERENCES categorias(id)
);