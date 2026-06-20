const RespuestaDTO = require('../dto/RespuestaDTO');
const conversacionesService = require('../service/conversaciones.service');

async function CrearConversacion(req, res, next) {
    try {
        const { iniciador_id, iniciador_tipo, receptor_id, receptor_tipo, alumno_id, curso_id } = req.body;
        const token = req.headers['authorization'];
        const data = await conversacionesService.crearConversacion(iniciador_id, iniciador_tipo, receptor_id, receptor_tipo, alumno_id, curso_id, token);
        const respuesta = new RespuestaDTO().ok(data, 'Conversacion creada exitosamente');
        return res.status(200).json(respuesta);
    } catch (error) {
        if (error.status) {
            const respuesta = new RespuestaDTO().error(error.status, error.message);
            return res.status(error.status).json(respuesta);
        }
        return res.status(500).json({ error: error.message });
    }
}

async function ObtenerPorUsuario(req, res, next) {
    try {
        const { usuarioId } = req.params;
        const token = req.headers['authorization'];
        const data = await conversacionesService.obtenerPorUsuario(usuarioId, token);
        const respuesta = new RespuestaDTO().ok(data);
        return res.status(200).json(respuesta);
    } catch (error) {
        return res.status(500).json({ error: error.message });
    }
}

module.exports = { CrearConversacion, ObtenerPorUsuario };
