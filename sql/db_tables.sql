CREATE DATABASE incidenciasAppBD;
USE incidenciasAppBD;

CREATE TABLE roles (
    id_rol INT AUTO_INCREMENT PRIMARY KEY,
    nombre VARCHAR(50) UNIQUE NOT NULL
);

SELECT * FROM roles;
delete from usuarios
where usuario != 'juanperez';

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

ALTER TABLE usuarios ADD COLUMN foto VARCHAR(255) NOT NULL;

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
select*from usuarios;
select*from usuarios;
UPDATE usuarios 
SET id_rol = 1
WHERE id_usuario = 1;




CREATE TABLE sectores (
    id_sector INT PRIMARY KEY AUTO_INCREMENT,
    nombre VARCHAR(100) UNIQUE NOT NULL
);

CREATE TABLE urbanizaciones (
    id_urbanizacion INT PRIMARY KEY AUTO_INCREMENT,
    nombre VARCHAR(100) UNIQUE NOT NULL,
    id_sector INT NOT NULL,
    CONSTRAINT fk_urbanizaciones_sector FOREIGN KEY (id_sector) REFERENCES sectores(id_sector) ON DELETE CASCADE
);

CREATE TABLE incidencias_urbanizaciones (
    id_incidencia INT,
    id_urbanizacion INT,
    PRIMARY KEY (id_incidencia, id_urbanizacion),
    CONSTRAINT fk_inc_urb_inc FOREIGN KEY (id_incidencia) REFERENCES incidencias(id_incidencia) ON DELETE CASCADE,
    CONSTRAINT fk_inc_urb_urb FOREIGN KEY (id_urbanizacion) REFERENCES urbanizaciones(id_urbanizacion) ON DELETE CASCADE
);

INSERT INTO delitos (nombre) VALUES
('Comercio ambulatorio'),
('Paradero informal'),
('Alteración del orden público'),
('Zona rígida'),
('Pérdida de personas'),
('Meretricio'),
('Consumo de bebidas alcohólicas - drogas'),
('Indigentes'),
('Movilización'),
('Alerta de robo / hurto'),
('Actos obscenos'),
('Cierre de calles'),
('Toldo en vía pública');


INSERT INTO servicios_serenazgo (nombre) VALUES
('Patrullaje preventivo en instituciones educativas'),
('Patrullaje preventivo en la comunidad'),
('Intervención en operativos conjuntos con otras autoridades'),
('Intervención en grescas, peleas o agresiones físicas'),
('Intervención en casos de personas sospechosas o vehículos sospechosos'),
('Apoyo en la identificación y retiro de vehículos abandonados'),
('Asistencia en accidentes de tránsito'),
('Rescate y búsqueda de personas extraviadas'),
('Atención y asistencia en incendios urbanos'),
('Asistencia en inundaciones y emergencias climáticas'),
('Intervención en colapso de estructuras e instalaciones peligrosas'),
('Apoyo mecánico en casos de emergencia vehicular'),
('Custodia y traslado humanitario de personas vulnerables'),
('Intervención en casos de violencia familiar y de género'),
('Intervención en alteraciones del orden por consumo de licor y drogas en la vía pública'),
('Apoyo en la erradicación de comercio ambulatorio no autorizado'),
('Intervención en desórdenes causados por personas en situación de calle con alteraciones mentales'),
('Ordenamiento del tránsito y seguridad vial'),
('Intervención en la quema ilegal de basura y contaminación ambiental'),
('Intervención por actos que afecten la higiene y salubridad pública'),
('Intervención en traslado de agraviados a comisarías'),
('Traslado asistido a hospitales y centros de salud'),
('Apoyo en custodia y traslado de cadáveres'),
('Intervención en eventos masivos para prevención de riesgos');

CREATE TABLE personas (
    id_persona INT PRIMARY KEY AUTO_INCREMENT,
    nombres VARCHAR(100),
    apellidos VARCHAR(100),
    n_doc char(15),
    telefono CHAR(9),
    direccion VARCHAR(100)
);

CREATE TABLE infractores (
    id_infractor INT PRIMARY KEY AUTO_INCREMENT,
    id_persona INT,
    id_incidencia INT,
    observaciones TEXT,
    FOREIGN KEY (id_persona) REFERENCES personas(id_persona),
    FOREIGN KEY (id_incidencia) REFERENCES incidencias(id_incidencia)
);

CREATE TABLE agraviados (
    id_agraviado INT PRIMARY KEY AUTO_INCREMENT,
    id_persona INT,
    id_incidencia INT,
    observaciones TEXT,
    FOREIGN KEY (id_persona) REFERENCES personas(id_persona),
    FOREIGN KEY (id_incidencia) REFERENCES incidencias(id_incidencia)
);

ALTER TABLE incidencias 
CHANGE COLUMN resultadoFinal conclusion TEXT NOT NULL;

ALTER TABLE incidencias
ADD COLUMN fecha_hora_registro DATETIME NOT NULL;

ALTER TABLE incidencias
DROP COLUMN id_unidad;

ALTER TABLE incidencias 
ADD COLUMN apoyo_policial ENUM('Si','No');

DROP TABLE unidades_apoyo;
ALTER TABLE incidencias
DROP COLUMN id_unidad; 

select*from tipos_ocurrencia;
select*from tipos_intervencion;
select*from delitos;
select*from incidencias;


ALTER TABLE personas
DROP COLUMN direccion;