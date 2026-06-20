const RespuestaDTO = require('../dto/RespuestaDTO');
const notificacionesService = require('../service/notificaciones.service');

async function CrearNotificacion(req, res, next) {
    try {
        const { usuario_id, mensaje } = req.body;
        const io = req.app.get('io');
        const data = await notificacionesService.crearNotificacion(usuario_id, mensaje, io);
        const respuesta = new RespuestaDTO().ok(data, 'Notificacion creada exitosamente');
        return res.status(200).json(respuesta);
    } catch (error) {
        return res.status(500).json({ error: error.message });
    }
}

async function ObtenerPorUsuario(req, res, next) {
    try {
        const { usuarioId } = req.params;
        const data = await notificacionesService.obtenerPorUsuario(usuarioId);
        const respuesta = new RespuestaDTO().ok(data);
        return res.status(200).json(respuesta);
    } catch (error) {
        return res.status(500).json({ error: error.message });
    }
}

async function MarcarComoLeida(req, res, next) {
    try {
        const { id } = req.params;
        const data = await notificacionesService.marcarComoLeida(id);
        const respuesta = new RespuestaDTO().ok(data, 'Notificacion marcada como leida');
        return res.status(200).json(respuesta);
    } catch (error) {
        return res.status(500).json({ error: error.message });
    }
}

module.exports = { CrearNotificacion, ObtenerPorUsuario, MarcarComoLeida };
