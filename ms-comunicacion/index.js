const http = require('http');
const app = require('./app');
const { Server } = require('socket.io');

const PORT = 3000;

const server = http.createServer(app);

const io = new Server(server, {
    cors: {
        origin: '*',
        methods: ['GET', 'POST']
    }
});

io.on('connection', (socket) => {
    socket.on('unirse', (usuarioId) => {
        socket.join(`usuario_${usuarioId}`);
        console.log(`Usuario ${usuarioId} unido a sala usuario_${usuarioId}`);
    });

    socket.on('disconnect', () => {
        console.log(`Socket desconectado: ${socket.id}`);
    });
});

app.set('io', io);

server.listen(PORT, () => {
    console.log(`API corriendo en puerto ${PORT}`);
});
