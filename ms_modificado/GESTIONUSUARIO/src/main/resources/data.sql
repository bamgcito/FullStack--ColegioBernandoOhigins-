INSERT IGNORE INTO roles (nombre) VALUES ('ADMIN'),('ALUMNO'),('PROFESOR'),('APODERADO');

INSERT IGNORE INTO usuarios (rut, contrasena, rol_id, fecha_creacion)
  SELECT '12345678-9',
         '$2b$10$lUrA1/neFR4JDkCemtSXWO9oW9K7zMlBZ7Je.dmZGK/DVA7YpNyZi',
         id,
         NOW()
  FROM roles WHERE nombre = 'ADMIN';