const RespuestaDTO = require('../dto/RespuestaDTO');
const mensajesService = require('../service/mensajes.service');

async function EnviarMensaje(req, res, next) {
    try {
        const { conversacion_id, emisor_id, emisor_tipo, contenido } = req.body;
        const token = req.headers['authorization'];
        const io = req.app.get('io');
        const data = await mensajesService.enviarMensaje(conversacion_id, emisor_id, emisor_tipo, contenido, token, io);
        const respuesta = new RespuestaDTO().ok(data, 'Mensaje enviado exitosamente');
        return res.status(200).json(respuesta);
    } catch (error) {
        return res.status(500).json({ error: error.message });
    }
}

async function ObtenerPorConversacion(req, res, next) {
    try {
        const { conversacionId } = req.params;
        const data = await mensajesService.obtenerPorConversacion(conversacionId);
        const respuesta = new RespuestaDTO().ok(data);
        return res.status(200).json(respuesta);
    } catch (error) {
        return res.status(500).json({ error: error.message });
    }
}

module.exports = { EnviarMensaje, ObtenerPorConversacion };
