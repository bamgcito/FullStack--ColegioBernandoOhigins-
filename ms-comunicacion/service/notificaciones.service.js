const { pool } = require('../database/mysql');

async function crearNotificacion(usuario_id, mensaje, io) {
    const [result] = await pool.execute(
        'INSERT INTO notificaciones (usuario_id, mensaje) VALUES (?, ?)',
        [usuario_id, mensaje]
    );

    const [rows] = await pool.execute(
        'SELECT * FROM notificaciones WHERE id = ?',
        [result.insertId]
    );

    const notificacion = rows[0] ?? {};

    if (io) {
        io.to(`usuario_${usuario_id}`).emit('nueva_notificacion', notificacion);
    }

    return {
        notificacion: notificacion
    };
}

async function obtenerPorUsuario(usuarioId) {
    const [rows] = await pool.execute(
        'SELECT * FROM notificaciones WHERE usuario_id = ? ORDER BY fecha_creacion DESC',
        [usuarioId]
    );

    return {
        lista_notificaciones: rows ?? []
    };
}

async function marcarComoLeida(id) {
    await pool.execute(
        'UPDATE notificaciones SET leido = TRUE WHERE id = ?',
        [id]
    );

    const [rows] = await pool.execute(
        'SELECT * FROM notificaciones WHERE id = ?',
        [id]
    );

    return {
        notificacion: rows[0] ?? {}
    };
}

module.exports = { crearNotificacion, obtenerPorUsuario, marcarComoLeida };
