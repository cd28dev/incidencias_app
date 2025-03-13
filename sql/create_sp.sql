use incidenciasappbd;
SELECT*FROM roles;
INSERT INTO roles(nombre)
VALUES("ROLE_ADMIN"),("ROLE_OPERADOR");

select * from Usuarios;

DELIMITER $$

CREATE PROCEDURE sp_insertar_usuario(
    IN p_nombre VARCHAR(100),
    IN p_apellido VARCHAR(100),
    IN p_dni VARCHAR(10),
    IN p_correo VARCHAR(150),
    IN p_usuario VARCHAR(50),
    IN p_password VARCHAR(255),
    IN p_id_rol INT
)
BEGIN
    DECLARE rol_existe INT;
    DECLARE usuario_existe INT;
    DECLARE correo_existe INT;
    
    START TRANSACTION;

    SELECT COUNT(*) INTO rol_existe FROM roles WHERE id_rol = p_id_rol;
    SELECT COUNT(*) INTO usuario_existe FROM usuarios WHERE usuario = p_usuario;
    SELECT COUNT(*) INTO correo_existe FROM usuarios WHERE correo = p_correo AND p_correo IS NOT NULL;

    IF rol_existe = 0 or usuario_existe > 0 or correo_existe > 0 THEN
        ROLLBACK;
        SIGNAL SQLSTATE '45000'
        SET MESSAGE_TEXT = 'Verifique username o correo';
    ELSE
        -- Insertar el usuario si todas las validaciones son correctas
        INSERT INTO usuarios (nombre, apellido, dni, correo, usuario, password, id_rol)
        VALUES (p_nombre, p_apellido, p_dni, p_correo, p_usuario, p_password, p_id_rol);
        
        -- Confirmar la transacción
        COMMIT;
    END IF;
END $$
DELIMITER ;


DELIMITER $$

CREATE PROCEDURE sp_actualizar_usuario(
    IN p_id_usuario INT,
    IN p_nombre VARCHAR(100),
    IN p_apellido VARCHAR(100),
    IN p_dni VARCHAR(10),
    IN p_correo VARCHAR(150),
    IN p_usuario VARCHAR(50),
    IN p_password VARCHAR(255),
    IN p_id_rol INT
)
BEGIN
    DECLARE usuario_existe INT;
    DECLARE rol_existe INT;
    DECLARE usuario_duplicado INT;
    DECLARE correo_duplicado INT;

    START TRANSACTION;

    -- Verificar si el usuario a actualizar existe
    SELECT COUNT(*) INTO usuario_existe FROM usuarios WHERE id_usuario = p_id_usuario;

    -- Verificar si el rol existe
    SELECT COUNT(*) INTO rol_existe FROM roles WHERE id_rol = p_id_rol;

    -- Verificar si el nuevo username ya está en uso por otro usuario
    SELECT COUNT(*) INTO usuario_duplicado 
    FROM usuarios 
    WHERE usuario = p_usuario AND id_usuario != p_id_usuario;

    -- Verificar si el nuevo correo ya está en uso por otro usuario (solo si no es NULL)
    IF p_correo IS NOT NULL THEN
        SELECT COUNT(*) INTO correo_duplicado 
        FROM usuarios 
        WHERE correo = p_correo AND id_usuario != p_id_usuario;
    ELSE
        SET correo_duplicado = 0;
    END IF;

    -- Validaciones
    IF usuario_existe = 0 THEN
        ROLLBACK;
        SIGNAL SQLSTATE '45000'
        SET MESSAGE_TEXT = 'Error: El usuario especificado no existe';

    ELSEIF rol_existe = 0 THEN
        ROLLBACK;
        SIGNAL SQLSTATE '45000'
        SET MESSAGE_TEXT = 'Error: El rol especificado no existe';

    ELSEIF usuario_duplicado > 0 THEN
        ROLLBACK;
        SIGNAL SQLSTATE '45000'
        SET MESSAGE_TEXT = 'Error: El nombre de usuario ya está en uso';

    ELSEIF correo_duplicado > 0 THEN
        ROLLBACK;
        SIGNAL SQLSTATE '45000'
        SET MESSAGE_TEXT = 'Error: El correo ya está registrado';

    ELSE
        -- Actualizar usuario
        UPDATE usuarios
        SET nombre = p_nombre,
            apellido = p_apellido,
            dni = p_dni,
            correo = p_correo,
            usuario = p_usuario,
            password = p_password,
            id_rol = p_id_rol
        WHERE id_usuario = p_id_usuario;

        COMMIT;

        -- Retornar los datos actualizados del usuario
        SELECT id_usuario, nombre, apellido, dni, correo, usuario, id_rol
        FROM usuarios
        WHERE id_usuario = p_id_usuario;
    END IF;
END $$

DELIMITER ;

DELIMITER $$

DELIMITER $$

select * from urbanizaciones;

CREATE PROCEDURE buscar_usuario_por_dni(
    IN p_dni VARCHAR(20)
)
BEGIN
    SELECT u.id, u.nombre, u.apellido, u.dni, u.email, u.telefono, r.nombre AS rol
    FROM usuarios u
    JOIN roles r ON u.id_rol = r.id_rol
    WHERE u.dni = p_dni;
END $$

DELIMITER ;


DELIMITER $$

CREATE PROCEDURE eliminar_usuario_por_dni(
    IN p_dni VARCHAR(10),
    OUT p_resultado INT
)
BEGIN
    DECLARE v_existe INT DEFAULT 0;

    -- Iniciar la transacción
    START TRANSACTION;

    -- Verificar si el usuario existe
    SELECT COUNT(*) INTO v_existe FROM usuarios WHERE dni = p_dni;

    IF v_existe > 0 THEN
        -- Eliminar usuario
        DELETE FROM usuarios WHERE dni = p_dni;
        
        -- Confirmar la eliminación
        COMMIT;
        SET p_resultado = 1; -- Usuario eliminado correctamente
    ELSE
        -- Si no existe, revertir la transacción
        ROLLBACK;
        SET p_resultado = 0; -- Usuario no encontrado
    END IF;
END $$

DELIMITER ;

DELIMITER $$

CREATE PROCEDURE sp_listar_usuarios()
BEGIN
    DECLARE total INT DEFAULT 0;

    -- Contar la cantidad de usuarios
    SELECT COUNT(*) INTO total FROM usuarios;

    -- Si no hay usuarios, lanzar un error
    IF total = 0 THEN
        SIGNAL SQLSTATE '45000'
        SET MESSAGE_TEXT = 'No hay usuarios registrados';
    END IF;

    -- Retornar la lista de usuarios con el nombre del rol
    SELECT u.id, u.nombre, u.apellido, u.dni, u.correo, u.usuario, r.nombre AS rol
    FROM usuarios u
    JOIN roles r ON u.id_rol = r.id_rol;
END $$

DELIMITER ;
select*from roles;


DELIMITER $$

CREATE PROCEDURE `sp_listar_delitos`()
BEGIN
    -- Retornar una lista vacía si no hay delitos
    IF (SELECT COUNT(*) FROM delitos) = 0 THEN
        SELECT NULL AS id_delito, NULL AS nombre
        FROM DUAL WHERE FALSE; -- Retorna 0 filas sin lanzar error
    ELSE
        -- Retornar la lista de roles
        SELECT id_delito, nombre FROM delitos;
    END IF;
END $$

DELIMITER ;


DELIMITER $$

CREATE PROCEDURE GetNextUserId()
BEGIN
    DECLARE next_id INT;

    SELECT AUTO_INCREMENT 
    INTO next_id 
    FROM information_schema.TABLES 
    WHERE TABLE_SCHEMA = 'incidenciasappbd' 
    AND TABLE_NAME = 'usuarios';

    SELECT next_id;
END $$

DELIMITER ;

DELIMITER //
CREATE PROCEDURE sp_validar_usuario(
    IN p_username VARCHAR(50),
    IN p_password VARCHAR(255),
    OUT p_existe BOOLEAN
)
BEGIN
    DECLARE v_count INT;
    
    SELECT COUNT(*) INTO v_count 
    FROM usuarios 
    WHERE username = p_username AND password = SHA2(p_password, 256);
    
    SET p_existe = (v_count > 0);
END //
DELIMITER ;


DELIMITER $$

CREATE PROCEDURE sp_insertar_delito(
    IN p_nombre VARCHAR(50)
)
BEGIN
    DECLARE delito_existe INT;
    
	START TRANSACTION;
    
    SELECT COUNT(*) INTO delito_existe FROM delitos WHERE nombre = p_nombre;
    
    IF delito_existe > 0 THEN
		ROLLBACK;
        SIGNAL SQLSTATE '45000'
        SET MESSAGE_TEXT = 'El delito ya existe';
    ELSE
        INSERT INTO delitos (nombre) VALUES (p_nombre);
        COMMIT;
    END IF;
END $$

DELIMITER ;


DELIMITER $$

CREATE PROCEDURE sp_buscar_delito_por_id(
    IN p_idDelito INT
)
BEGIN
    SELECT id_delito, nombre
    FROM delitos
    WHERE id_delito = p_idDelito;
END $$

DELIMITER ;

DELIMITER $$

CREATE DEFINER=`root`@`localhost` PROCEDURE `sp_actualizar_Delito`(
    IN p_idDelito INT,
    IN p_nombre VARCHAR(50)
)
BEGIN
    DECLARE delito_existe INT;
    DECLARE nombre_duplicado INT;

    START TRANSACTION;

    -- Verificar si el rol a actualizar existe
    SELECT COUNT(*) INTO delito_existe FROM delitos WHERE id_delito = p_idDelito;

    -- Verificar si el nuevo nombre ya está en uso por otro rol
    SELECT COUNT(*) INTO nombre_duplicado FROM delitos WHERE nombre = p_nombre AND id_delito != p_idDelito;

    -- Validaciones
    IF delito_existe = 0 THEN
        ROLLBACK;
        SIGNAL SQLSTATE '45000'
        SET MESSAGE_TEXT = 'Error: El delito especificado no existe';

    ELSEIF nombre_duplicado > 0 THEN
        ROLLBACK;
        SIGNAL SQLSTATE '45000'
        SET MESSAGE_TEXT = 'Error: El nombre del rol ya está en uso';

    ELSE
        -- Actualizar rol
        UPDATE delitos
        SET nombre = p_nombre
        WHERE id_delito = p_idDelito;

        COMMIT;

        -- Retornar los datos actualizados del rol
        SELECT id_Delito, nombre FROM delitos WHERE id_delito = p_idDelito;
    END IF;
END $$

DELIMITER ;

DELIMITER $$

CREATE PROCEDURE sp_eliminar_delito(
    IN p_id_delito INT,
    OUT p_resultado INT
)
BEGIN
    DECLARE v_existe INT DEFAULT 0;

    -- Manejar errores de SQL
    DECLARE CONTINUE HANDLER FOR SQLEXCEPTION 
    BEGIN
        ROLLBACK;
        SET p_resultado = -1; -- Error al eliminar
    END;

    -- Iniciar la transacción
    START TRANSACTION;

    -- Verificar si el rol existe
    SELECT COUNT(*) INTO v_existe FROM delitos WHERE id_delito = p_id_delito;

    IF v_existe > 0 THEN
        -- Eliminar el rol
        DELETE FROM delitos WHERE id_delito = p_id_delito;
        
        -- Confirmar eliminación
        COMMIT;
        SET p_resultado = 1; -- Eliminado correctamente
    ELSE
        -- Si no existe, deshacer la transacción
        ROLLBACK;
        SET p_resultado = 0; -- No encontrado
    END IF;

END $$

DELIMITER ;


DELIMITER $$
-- Listar todos los sectores
CREATE DEFINER=`root`@`localhost` PROCEDURE `sp_listar_sectores`()
BEGIN
    -- Retornar una lista vacía si no hay sectores
    IF (SELECT COUNT(*) FROM sectores) = 0 THEN
        SELECT NULL AS id_sector, NULL AS nombre
        FROM DUAL WHERE FALSE; -- Retorna 0 filas sin lanzar error
    ELSE
        -- Retornar la lista de sectores
        SELECT id_sector, nombre FROM sectores;
    END IF;
END $$

DELIMITER ;
DELIMITER $$
-- Insertar un nuevo sector
CREATE DEFINER=`root`@`localhost` PROCEDURE `sp_insertar_sector`(
    IN p_nombre VARCHAR(50)
)
BEGIN
    DECLARE sector_existe INT;
    DECLARE EXIT HANDLER FOR SQLEXCEPTION
    BEGIN
        -- Si ocurre un error, se revierte la transacción
        ROLLBACK;
        SIGNAL SQLSTATE '45000'
        SET MESSAGE_TEXT = 'Error al insertar el sector';
    END;

    START TRANSACTION;
    
    -- Verificar si ya existe un sector con el mismo nombre
    SELECT COUNT(*) INTO sector_existe FROM sectores WHERE nombre = p_nombre FOR UPDATE;
    
    IF sector_existe > 0 THEN
        -- Si el sector ya existe, se revierte la transacción
        ROLLBACK;
        SIGNAL SQLSTATE '45000'
        SET MESSAGE_TEXT = 'El sector ya existe';
    ELSE
        -- Insertar el nuevo sector
        INSERT INTO sectores (nombre) VALUES (p_nombre);
        COMMIT;
    END IF;
END $$

DELIMITER ;
DELIMITER $$
-- Eliminar un sector por ID
CREATE DEFINER=`root`@`localhost` PROCEDURE `sp_eliminar_sector`(
    IN p_id_sector INT,
    OUT p_resultado INT
)
BEGIN
    DECLARE v_existe INT DEFAULT 0;

    -- Manejar errores de SQL
    DECLARE CONTINUE HANDLER FOR SQLEXCEPTION 
    BEGIN
        ROLLBACK;
        SET p_resultado = -1; -- Error al eliminar
    END;

    -- Iniciar la transacción
    START TRANSACTION;

    -- Verificar si el sector existe
    SELECT COUNT(*) INTO v_existe FROM sectores WHERE id_sector = p_id_sector;

    IF v_existe > 0 THEN
        -- Eliminar el sector
        DELETE FROM sectores WHERE id_sector = p_id_sector;
        
        -- Confirmar eliminación
        COMMIT;
        SET p_resultado = 1; -- Eliminado correctamente
    ELSE
        -- Si no existe, deshacer la transacción
        ROLLBACK;
        SET p_resultado = 0; -- No encontrado
    END IF;

END $$

DELIMITER ;

DELIMITER $$
-- Actualizar un sector por ID
CREATE DEFINER=`root`@`localhost` PROCEDURE `sp_actualizar_sector`(
    IN p_idSector INT,
    IN p_nombre VARCHAR(50)
)
BEGIN
    DECLARE sector_existe INT;
    DECLARE nombre_duplicado INT;

    START TRANSACTION;

    -- Verificar si el sector a actualizar existe
    SELECT COUNT(*) INTO sector_existe FROM sectores WHERE id_sector = p_idSector;

    -- Verificar si el nuevo nombre ya está en uso por otro sector
    SELECT COUNT(*) INTO nombre_duplicado FROM sectores WHERE nombre = p_nombre AND id_sector != p_idSector;

    -- Validaciones
    IF sector_existe = 0 THEN
        ROLLBACK;
        SIGNAL SQLSTATE '45000'
        SET MESSAGE_TEXT = 'Error: El sector especificado no existe';

    ELSEIF nombre_duplicado > 0 THEN
        ROLLBACK;
        SIGNAL SQLSTATE '45000'
        SET MESSAGE_TEXT = 'Error: El nombre del sector ya está en uso';

    ELSE
        -- Actualizar sector
        UPDATE sectores
        SET nombre = p_nombre
        WHERE id_sector = p_idSector;

        COMMIT;

        -- Retornar los datos actualizados del sector
        SELECT id_sector, nombre FROM sectores WHERE id_sector = p_idSector;
    END IF;
END $$

DELIMITER ;

DELIMITER $$
-- Buscar un sector por ID
CREATE DEFINER=`root`@`localhost` PROCEDURE `sp_buscar_sector_por_id`(
    IN p_idSector INT
)
BEGIN
    SELECT id_sector, nombre
    FROM sectores
    WHERE id_sector = p_idSector;
END $$

DELIMITER ;



DELIMITER $$

-- Listar todas las urbanizaciones
CREATE DEFINER=`root`@`localhost` PROCEDURE `sp_listar_urbanizaciones`()
BEGIN
    -- Retornar una lista vacía si no hay urbanizaciones
    IF (SELECT COUNT(*) FROM urbanizaciones) = 0 THEN
        SELECT NULL AS id_urbanizacion, NULL AS nombre, NULL AS id_sector
        FROM DUAL WHERE FALSE; -- Retorna 0 filas sin lanzar error
    ELSE
        -- Retornar la lista de urbanizaciones
        SELECT u.id_urbanizacion, u.nombre, u.id_sector, s.nombre
        FROM urbanizaciones as u
        INNER JOIN sectores as s ON s.id_sector=u.id_sector;
    END IF;
END $$

DELIMITER ;

DELIMITER $$

CREATE DEFINER=`root`@`localhost` PROCEDURE `sp_insertar_urbanizacion`(
    IN p_nombre VARCHAR(50),
    IN p_id_sector INT
)
BEGIN
    DECLARE urbanizacion_existe INT;
    DECLARE sector_existe INT;
    
    DECLARE EXIT HANDLER FOR SQLEXCEPTION
    BEGIN
        -- Si ocurre un error, se revierte la transacción
        ROLLBACK;
        SIGNAL SQLSTATE '45000'
        SET MESSAGE_TEXT = 'Error al insertar la urbanización';
    END;

    START TRANSACTION;
    
    -- Verificar si el sector existe
    SELECT COUNT(*) INTO sector_existe FROM sectores WHERE id_sector = p_id_sector;
    
    IF sector_existe = 0 THEN
        ROLLBACK;
        SIGNAL SQLSTATE '45000'
        SET MESSAGE_TEXT = 'El sector especificado no existe';
    END IF;

    -- Verificar si ya existe una urbanización con el mismo nombre en el sector
    SELECT COUNT(*) INTO urbanizacion_existe 
    FROM urbanizaciones 
    WHERE nombre = p_nombre AND id_sector = p_id_sector FOR UPDATE;
    
    IF urbanizacion_existe > 0 THEN
        -- Si la urbanización ya existe, se revierte la transacción
        ROLLBACK;
        SIGNAL SQLSTATE '45000'
        SET MESSAGE_TEXT = 'La urbanización ya existe en este sector';
    ELSE
        -- Insertar la nueva urbanización
        INSERT INTO urbanizaciones (nombre, id_sector) VALUES (p_nombre, p_id_sector);
        COMMIT;
    END IF;
END $$

DELIMITER ;

DELIMITER $$

-- Eliminar una urbanización por ID
CREATE DEFINER=`root`@`localhost` PROCEDURE `sp_eliminar_urbanizacion`(
    IN p_id_urbanizacion INT,
    OUT p_resultado INT
)
BEGIN
    DECLARE v_existe INT DEFAULT 0;

    -- Manejar errores de SQL
    DECLARE CONTINUE HANDLER FOR SQLEXCEPTION 
    BEGIN
        ROLLBACK;
        SET p_resultado = -1; -- Error al eliminar
    END;

    -- Iniciar la transacción
    START TRANSACTION;

    -- Verificar si la urbanización existe
    SELECT COUNT(*) INTO v_existe FROM urbanizaciones WHERE id_urbanizacion = p_id_urbanizacion;

    IF v_existe > 0 THEN
        -- Eliminar la urbanización
        DELETE FROM urbanizaciones WHERE id_urbanizacion = p_id_urbanizacion;
        
        -- Confirmar eliminación
        COMMIT;
        SET p_resultado = 1; -- Eliminado correctamente
    ELSE
        -- Si no existe, deshacer la transacción
        ROLLBACK;
        SET p_resultado = 0; -- No encontrado
    END IF;
END $$

DELIMITER ;


DELIMITER $$

-- Actualizar una urbanización por ID
CREATE DEFINER=`root`@`localhost` PROCEDURE `sp_actualizar_urbanizacion`(
    IN p_id_urbanizacion INT,
    IN p_nombre VARCHAR(50),
    IN p_id_sector INT
)
BEGIN
    DECLARE urbanizacion_existe INT;
    DECLARE nombre_duplicado INT;
    DECLARE sector_existe INT;

    START TRANSACTION;

    -- Verificar si la urbanización a actualizar existe
    SELECT COUNT(*) INTO urbanizacion_existe FROM urbanizaciones WHERE id_urbanizacion = p_id_urbanizacion;

    -- Verificar si el nuevo nombre ya está en uso en el mismo sector
    SELECT COUNT(*) INTO nombre_duplicado FROM urbanizaciones 
    WHERE nombre = p_nombre AND id_sector = p_id_sector AND id_urbanizacion != p_id_urbanizacion;

    -- Verificar si el sector existe
    SELECT COUNT(*) INTO sector_existe FROM sectores WHERE id_sector = p_id_sector;

    -- Validaciones
    IF urbanizacion_existe = 0 THEN
        ROLLBACK;
        SIGNAL SQLSTATE '45000'
        SET MESSAGE_TEXT = 'Error: La urbanización especificada no existe';

    ELSEIF nombre_duplicado > 0 THEN
        ROLLBACK;
        SIGNAL SQLSTATE '45000'
        SET MESSAGE_TEXT = 'Error: El nombre de la urbanización ya está en uso en este sector';

    ELSEIF sector_existe = 0 THEN
        ROLLBACK;
        SIGNAL SQLSTATE '45000'
        SET MESSAGE_TEXT = 'Error: El sector especificado no existe';

    ELSE
        -- Actualizar urbanización
        UPDATE urbanizaciones
        SET nombre = p_nombre, id_sector = p_id_sector
        WHERE id_urbanizacion = p_id_urbanizacion;

        COMMIT;

        -- Retornar los datos actualizados de la urbanización
        SELECT id_urbanizacion, nombre, id_sector FROM urbanizaciones WHERE id_urbanizacion = p_id_urbanizacion;
    END IF;
END $$

DELIMITER ;


DELIMITER $$

-- Buscar una urbanización por ID
CREATE DEFINER=`root`@`localhost` PROCEDURE `sp_buscar_urbanizacion_por_id`(
    IN p_id_urbanizacion INT
)
BEGIN
    SELECT u.id_urbanizacion, u.nombre, s.id_sector,s.nombre
    FROM urbanizaciones as u
    INNER JOIN sectores as s ON u.id_sector=s.id_sector
    WHERE u.id_urbanizacion = u.p_id_urbanizacion;
END $$

DELIMITER ;