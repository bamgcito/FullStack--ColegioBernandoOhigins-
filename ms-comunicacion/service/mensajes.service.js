const { pool } = require('../database/mysql');
const { obtenerNombrePerfil } = require('./perfiles.helper');
const notificacionesService = require('./notificaciones.service');

async function enviarMensaje(conversacion_id, emisor_id, emisor_tipo, contenido, token, io) {
    const [result] = await pool.execute(
        'INSERT INTO mensajes (conversacion_id, emisor_id, emisor_tipo, contenido) VALUES (?, ?, ?, ?)',
        [conversacion_id, emisor_id, emisor_tipo, contenido]
    );

    const [rows] = await pool.execute(
        'SELECT * FROM mensajes WHERE id = ?',
        [result.insertId]
    );

    const mensaje = rows[0] ?? {};

    const [convRows] = await pool.execute(
        'SELECT * FROM conversaciones WHERE id = ?',
        [conversacion_id]
    );

    if (convRows.length > 0) {
        const conversacion = convRows[0];
        const receptorId = String(emisor_id) === String(conversacion.iniciador_id)
            ? conversacion.receptor_id
            : conversacion.iniciador_id;

        if (io) io.to(`usuario_${receptorId}`).emit('nuevo_mensaje', mensaje);

        const nombreEmisor = await obtenerNombrePerfil(emisor_tipo, emisor_id, token);
        const resumen = contenido.length > 60 ? contenido.slice(0, 60) + '…' : contenido;
        const texto = `Nuevo mensaje de ${nombreEmisor || 'alguien'}: ${resumen}`;
        await notificacionesService.crearNotificacion(receptorId, texto, io);
    }

    return {
        mensaje: mensaje
    };
}

async function obtenerPorConversacion(conversacionId) {
    const [rows] = await pool.execute(
        'SELECT * FROM mensajes WHERE conversacion_id = ? ORDER BY fecha_envio ASC',
        [conversacionId]
    );

    return {
        lista_mensajes: rows ?? []
    };
}

module.exports = { enviarMensaje, obtenerPorConversacion };
