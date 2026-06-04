CREATE DATABASE IF NOT EXISTS gestion_usuario CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;
CREATE DATABASE IF NOT EXISTS cursos_db CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;
CREATE DATABASE IF NOT EXISTS academico_db CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;
CREATE DATABASE IF NOT EXISTS asistencia_db CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;

GRANT ALL PRIVILEGES ON *.* TO 'root'@'%' WITH GRANT OPTION;
FLUSH PRIVILEGES;

-- Seed inicial: roles y usuario admin
USE gestion_usuario;

INSERT IGNORE INTO roles (nombre) VALUES ('ADMIN'),('ALUMNO'),('PROFESOR'),('APODERADO');

-- admin123 con BCrypt $2b$10
INSERT IGNORE INTO usuarios (rut, contrasena, rol_id, fecha_creacion)
VALUES ('12345678-9',
        '$2b$10$lUrA1/neFR4JDkCemtSXWO9oW9K7zMlBZ7Je.dmZGK/DVA7YpNyZi',
        (SELECT id FROM roles WHERE nombre = 'ADMIN'),
        NOW());