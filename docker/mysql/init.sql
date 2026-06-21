CREATE DATABASE IF NOT EXISTS cursos_db CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;
CREATE DATABASE IF NOT EXISTS asistencia_db CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;
CREATE DATABASE IF NOT EXISTS usuarios_db CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;
CREATE DATABASE IF NOT EXISTS perfiles_db CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;
CREATE DATABASE IF NOT EXISTS asignaturas_db CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;
CREATE DATABASE IF NOT EXISTS evaluaciones_db CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;
CREATE DATABASE IF NOT EXISTS notas_db CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;
CREATE DATABASE IF NOT EXISTS comunicacion_db CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;
CREATE DATABASE IF NOT EXISTS certificados_db CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;
CREATE DATABASE IF NOT EXISTS horarios_db CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;

GRANT ALL PRIVILEGES ON *.* TO 'root'@'%' WITH GRANT OPTION;
FLUSH PRIVILEGES;

USE comunicacion_db;

CREATE TABLE IF NOT EXISTS conversaciones (
    id INT AUTO_INCREMENT PRIMARY KEY,
    iniciador_id BIGINT NOT NULL,
    iniciador_tipo ENUM('APODERADO', 'ALUMNO', 'PROFESOR') NOT NULL,
    receptor_id BIGINT NOT NULL,
    receptor_tipo ENUM('PROFESOR_JEFE', 'PROFESOR_ASIGNATURA', 'APODERADO') NOT NULL,
    alumno_id BIGINT NOT NULL,
    fecha_creacion DATETIME DEFAULT CURRENT_TIMESTAMP
);

CREATE TABLE IF NOT EXISTS mensajes (
    id INT AUTO_INCREMENT PRIMARY KEY,
    conversacion_id INT NOT NULL,
    emisor_id BIGINT NOT NULL,
    emisor_tipo ENUM('APODERADO', 'PROFESOR', 'ALUMNO') NOT NULL,
    contenido TEXT NOT NULL,
    fecha_envio DATETIME DEFAULT CURRENT_TIMESTAMP,
    leido BOOLEAN DEFAULT FALSE,
    FOREIGN KEY (conversacion_id) REFERENCES conversaciones(id)
);

CREATE TABLE IF NOT EXISTS notificaciones (
    id INT AUTO_INCREMENT PRIMARY KEY,
    usuario_id BIGINT NOT NULL,
    mensaje TEXT NOT NULL,
    leido BOOLEAN DEFAULT FALSE,
    fecha_creacion DATETIME DEFAULT CURRENT_TIMESTAMP
);

-- Seed inicial: roles y usuario admin
USE usuarios_db;

INSERT IGNORE INTO roles (nombre) VALUES ('ADMIN'),('ALUMNO'),('PROFESOR'),('APODERADO');

-- admin123 con BCrypt $2b$10
INSERT IGNORE INTO usuarios (rut, contrasena, rol_id, fecha_creacion)
VALUES ('12345678-9',
        '$2b$10$lUrA1/neFR4JDkCemtSXWO9oW9K7zMlBZ7Je.dmZGK/DVA7YpNyZi',
        (SELECT id FROM roles WHERE nombre = 'ADMIN'),
        NOW());