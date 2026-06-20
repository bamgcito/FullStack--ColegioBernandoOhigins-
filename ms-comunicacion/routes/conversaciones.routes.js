const router = require('express').Router();
const controller = require('../controller/conversaciones.controller');
const auth = require('../middleware/authMiddleware');

router.post('/', auth, controller.CrearConversacion);
router.get('/usuario/:usuarioId', auth, controller.ObtenerPorUsuario);

module.exports = router;
