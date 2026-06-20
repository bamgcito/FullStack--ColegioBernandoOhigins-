const express = require('express');
const cors = require('cors');

const conversacionesRoutes = require('./routes/conversaciones.routes');
const mensajesRoutes = require('./routes/mensajes.routes');
const notificacionesRoutes = require('./routes/notificaciones.routes');

const app = express();

app.use(cors({
    origin: '*',
    methods: ['GET', 'POST', 'PUT', 'PATCH', 'DELETE', 'OPTIONS'],
    allowedHeaders: ['Content-Type', 'Authorization'],
}));

app.use(express.json());

app.use('/comunicacion/conversaciones', conversacionesRoutes);
app.use('/comunicacion/mensajes', mensajesRoutes);
app.use('/comunicacion/notificaciones', notificacionesRoutes);

module.exports = app;
