CREATE DATABASE IF NOT EXISTS comunicacion_db;
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
