const router = require('express').Router();
const controller = require('../controller/mensajes.controller');
const auth = require('../middleware/authMiddleware');

router.post('/', auth, controller.EnviarMensaje);
router.get('/conversacion/:conversacionId', auth, controller.ObtenerPorConversacion);

module.exports = router;
