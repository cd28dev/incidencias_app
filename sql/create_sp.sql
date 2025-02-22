use incidenciasappbd;
SELECT*FROM roles;
INSERT INTO roles(nombre)
VALUES("ROLE_ADMIN"),("ROLE_OPERADOR");

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


