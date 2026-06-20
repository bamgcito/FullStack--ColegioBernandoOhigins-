const router = require('express').Router();
const controller = require('../controller/notificaciones.controller');
const auth = require('../middleware/authMiddleware');

router.post('/', auth, controller.CrearNotificacion);
router.get('/usuario/:usuarioId', auth, controller.ObtenerPorUsuario);
router.patch('/:id/leer', auth, controller.MarcarComoLeida);

module.exports = router;
