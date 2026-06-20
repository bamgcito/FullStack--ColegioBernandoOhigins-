const jwt = require('jsonwebtoken');

const SECRET = 'clave-secreta-super-segura-para-jwt-minimo-32-chars';

function authMiddleware(req, res, next) {
    const header = req.headers['authorization'];

    if (!header || !header.startsWith('Bearer ')) {
        return res.status(401).json({ error: 'Token no proporcionado' });
    }

    const token = header.substring(7);

    try {
        const payload = jwt.verify(token, SECRET);
        req.usuario = payload;
        next();
    } catch (error) {
        return res.status(401).json({ error: 'Token inválido o expirado' });
    }
}

module.exports = authMiddleware;
