CREATE DATABASE incidenciasAppBD;
USE incidenciasAppBD;

CREATE TABLE roles (
    id_rol INT AUTO_INCREMENT PRIMARY KEY,
    nombre VARCHAR(50) UNIQUE NOT NULL
);

CREATE TABLE usuarios (
    id_usuario INT AUTO_INCREMENT PRIMARY KEY,
    nombre VARCHAR(100) NOT NULL,
    apellido VARCHAR(100) NOT NULL,
    dni VARCHAR(10) UNIQUE NOT NULL,
    correo VARCHAR(150) UNIQUE,
    usuario VARCHAR(50) UNIQUE NOT NULL,
    password VARCHAR(255) NOT NULL,
    id_rol INT NOT NULL,
    FOREIGN KEY (id_rol) REFERENCES roles(id_rol) ON DELETE CASCADE
);

CREATE TABLE unidades_apoyo (
    id_unidad INT AUTO_INCREMENT PRIMARY KEY,
    nombre VARCHAR(100) UNIQUE NOT NULL
);

CREATE TABLE incidencias (
    id_incidencia INT AUTO_INCREMENT PRIMARY KEY,
    direccion VARCHAR(255) NOT NULL,
    sector VARCHAR(100) NOT NULL,
    urbanizacion VARCHAR(100),
    fecha DATE NOT NULL,
    hora TIME NOT NULL,
    manifestacion TEXT,
    descripcion TEXT,
    resultadofinal TEXT,
    id_unidad INT NOT NULL,
    id_usuario INT NOT NULL, 
    FOREIGN KEY (id_unidad) REFERENCES unidades_apoyo(id_unidad) ON DELETE CASCADE,
    FOREIGN KEY (id_usuario) REFERENCES usuarios(id_usuario) ON DELETE CASCADE
);

CREATE TABLE tipos_ocurrencia (
    id_ocurrencia INT AUTO_INCREMENT PRIMARY KEY,
    nombre VARCHAR(100) UNIQUE NOT NULL
);

CREATE TABLE tipos_intervencion (
    id_intervencion INT AUTO_INCREMENT PRIMARY KEY,
    nombre VARCHAR(100) UNIQUE NOT NULL
);

CREATE TABLE incidencias_ocurrencias (
    id_incidencia INT,
    id_ocurrencia INT,
    PRIMARY KEY (id_incidencia, id_ocurrencia),
    FOREIGN KEY (id_incidencia) REFERENCES incidencias(id_incidencia) ON DELETE CASCADE,
    FOREIGN KEY (id_ocurrencia) REFERENCES tipos_ocurrencia(id_ocurrencia) ON DELETE CASCADE
);

CREATE TABLE incidencias_intervenciones (
    id_incidencia INT,
    id_intervencion INT,
    PRIMARY KEY (id_incidencia, id_intervencion),
    FOREIGN KEY (id_incidencia) REFERENCES incidencias(id_incidencia) ON DELETE CASCADE,
    FOREIGN KEY (id_intervencion) REFERENCES tipos_intervencion(id_intervencion) ON DELETE CASCADE
);

CREATE TABLE delitos (
    id_delito INT AUTO_INCREMENT PRIMARY KEY,
    nombre VARCHAR(255) UNIQUE NOT NULL
);

CREATE TABLE incidencias_delitos (
    id_incidencia INT,
    id_delito INT,
    PRIMARY KEY (id_incidencia, id_delito),
    FOREIGN KEY (id_incidencia) REFERENCES incidencias(id_incidencia) ON DELETE CASCADE,
    FOREIGN KEY (id_delito) REFERENCES delitos(id_delito) ON DELETE CASCADE
);

CREATE TABLE servicios_serenazgo (
    id_servicio INT AUTO_INCREMENT PRIMARY KEY,
    nombre VARCHAR(255) UNIQUE NOT NULL
);

CREATE TABLE incidencias_servicios (
    id_incidencia INT,
    id_servicio INT,
    PRIMARY KEY (id_incidencia, id_servicio),
    FOREIGN KEY (id_incidencia) REFERENCES incidencias(id_incidencia) ON DELETE CASCADE,
    FOREIGN KEY (id_servicio) REFERENCES servicios_serenazgo(id_servicio) ON DELETE CASCADE
);
