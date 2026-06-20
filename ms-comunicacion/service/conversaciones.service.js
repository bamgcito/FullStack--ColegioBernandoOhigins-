const { pool } = require('../database/mysql');
const { obtenerNombrePerfil, MS_PERFILES } = require('./perfiles.helper');

const MS_CURSOS = 'http://ms-cursos:8088';
const MS_ASIGNATURAS = 'http://ms-asignatura:8089';

async function obtenerPerfilAlumnoPorUsuario(usuarioId, token) {
    let response;
    try {
        response = await fetch(`${MS_PERFILES}/perfiles/alumnos/${usuarioId}`, {
            headers: { 'Authorization': token }
        });
    } catch {
        throw { status: 503, message: 'ms-perfiles no está disponible' };
    }

    if (response.status === 404) {
        throw { status: 404, message: `No se encontró el alumno con ID de usuario: ${usuarioId}` };
    }
    if (!response.ok) {
        throw { status: 503, message: 'Error al verificar el alumno en ms-perfiles' };
    }

    const texto = await response.text();
    const perfil = texto ? JSON.parse(texto) : null;
    if (!perfil || !perfil.id) {
        throw { status: 404, message: `No se encontró el alumno con ID de usuario: ${usuarioId}` };
    }
    return perfil;
}

async function verificarAccesoApoderado(apoderadoId, alumnoId, token) {
    let response;
    try {
        response = await fetch(`${MS_PERFILES}/perfiles/apoderados/${apoderadoId}/alumnos`, {
            headers: { 'Authorization': token }
        });
    } catch {
        throw { status: 503, message: 'ms-perfiles no está disponible' };
    }

    if (response.status === 404) {
        throw { status: 404, message: `No se encontró el apoderado con ID: ${apoderadoId}` };
    }
    if (!response.ok) {
        throw { status: 503, message: 'Error al verificar acceso del apoderado en ms-perfiles' };
    }

    const alumnos = await response.json();
    const tieneAcceso = Array.isArray(alumnos) && alumnos.some(
        a => String(a.id) === String(alumnoId) || String(a.alumnoId) === String(alumnoId)
    );

    if (!tieneAcceso) {
        throw { status: 403, message: `El apoderado ${apoderadoId} no tiene acceso al alumno con ID: ${alumnoId}` };
    }
}

async function verificarProfesorJefe(cursoId, receptorId, token) {
    let response;
    try {
        response = await fetch(`${MS_CURSOS}/cursos/${cursoId}`, {
            headers: { 'Authorization': token }
        });
    } catch {
        throw { status: 503, message: 'ms-cursos no está disponible' };
    }

    if (response.status === 404) {
        throw { status: 404, message: `No se encontró el curso con ID: ${cursoId}` };
    }
    if (!response.ok) {
        throw { status: 503, message: 'Error al obtener información del curso en ms-cursos' };
    }

    const texto = await response.text();
    const curso = texto ? JSON.parse(texto) : null;
    if (!curso || !curso.id) {
        throw { status: 404, message: `No se encontró el curso con ID: ${cursoId}` };
    }
    const profesorJefeId = curso.profesorJefeId ?? curso.profesor_jefe_id;

    if (!profesorJefeId || String(profesorJefeId) !== String(receptorId)) {
        throw { status: 400, message: `El receptor ${receptorId} no es el profesor jefe del curso ${cursoId}` };
    }
}

async function verificarProfesorAsignatura(cursoId, receptorId, token) {
    let response;
    try {
        response = await fetch(`${MS_ASIGNATURAS}/asignaciones/curso/${cursoId}`, {
            headers: { 'Authorization': token }
        });
    } catch {
        throw { status: 503, message: 'ms-asignaturas no está disponible' };
    }

    if (response.status === 404) {
        throw { status: 404, message: `No se encontraron asignaciones para el curso con ID: ${cursoId}` };
    }
    if (!response.ok) {
        throw { status: 503, message: 'Error al obtener asignaciones en ms-asignaturas' };
    }

    const asignaciones = await response.json();
    const esProfesor = Array.isArray(asignaciones) && asignaciones.some(
        a => String(a.profesorId) === String(receptorId) || String(a.profesor_id) === String(receptorId)
    );

    if (!esProfesor) {
        throw { status: 400, message: `El receptor ${receptorId} no tiene asignaciones en el curso ${cursoId}` };
    }
}

async function verificarProfesorEnCurso(cursoId, profesorId, token) {
    let response;
    try {
        response = await fetch(`${MS_CURSOS}/cursos/${cursoId}`, {
            headers: { 'Authorization': token }
        });
    } catch {
        throw { status: 503, message: 'ms-cursos no está disponible' };
    }

    if (!response.ok) {
        throw { status: 503, message: 'Error al obtener información del curso en ms-cursos' };
    }

    const texto = await response.text();
    const curso = texto ? JSON.parse(texto) : null;
    if (!curso || !curso.id) {
        throw { status: 404, message: `No se encontró el curso con ID: ${cursoId}` };
    }
    if (String(curso.profesorJefeId) === String(profesorId)) {
        return;
    }

    try {
        await verificarProfesorAsignatura(cursoId, profesorId, token);
    } catch {
        throw { status: 403, message: `El profesor ${profesorId} no tiene relación con el curso ${cursoId}` };
    }
}

async function verificarAlumnoEnCurso(cursoId, alumnoUsuarioId, token) {
    let response;
    try {
        response = await fetch(`${MS_CURSOS}/cursos/${cursoId}/alumnos`, {
            headers: { 'Authorization': token }
        });
    } catch {
        throw { status: 503, message: 'ms-cursos no está disponible' };
    }
    if (!response.ok) {
        throw { status: 503, message: 'Error al obtener alumnos del curso en ms-cursos' };
    }

    const alumnos = await response.json();
    const pertenece = Array.isArray(alumnos) && alumnos.some(a => String(a.alumnoId) === String(alumnoUsuarioId));

    if (!pertenece) {
        throw { status: 400, message: `El alumno ${alumnoUsuarioId} no pertenece al curso ${cursoId}` };
    }
}

async function verificarApoderadoDeAlumno(alumnoUsuarioId, receptorId, token) {
    let response;
    try {
        response = await fetch(`${MS_PERFILES}/perfiles/alumnos/${alumnoUsuarioId}/apoderados`, {
            headers: { 'Authorization': token }
        });
    } catch {
        throw { status: 503, message: 'ms-perfiles no está disponible' };
    }
    if (!response.ok) {
        throw { status: 503, message: 'Error al obtener apoderados del alumno en ms-perfiles' };
    }

    const apoderados = await response.json();
    const esApoderado = Array.isArray(apoderados) && apoderados.some(a => String(a.usuarioId) === String(receptorId));

    if (!esApoderado) {
        throw { status: 400, message: `El receptor ${receptorId} no es apoderado del alumno con ID de usuario: ${alumnoUsuarioId}` };
    }
}

async function crearConversacion(iniciador_id, iniciador_tipo, receptor_id, receptor_tipo, alumno_id, curso_id, token) {
    let alumnoIdFinal = alumno_id;

    if (iniciador_tipo === 'APODERADO') {
        await verificarAccesoApoderado(iniciador_id, alumno_id, token);
    } else if (iniciador_tipo === 'ALUMNO') {
        const perfil = await obtenerPerfilAlumnoPorUsuario(iniciador_id, token);
        alumnoIdFinal = perfil.id;
    } else if (iniciador_tipo === 'PROFESOR') {
        await verificarProfesorEnCurso(curso_id, iniciador_id, token);
        await verificarAlumnoEnCurso(curso_id, alumno_id, token);
        const perfil = await obtenerPerfilAlumnoPorUsuario(alumno_id, token);
        alumnoIdFinal = perfil.id;
    } else {
        throw { status: 400, message: `Tipo de iniciador inválido: ${iniciador_tipo}. Debe ser APODERADO, ALUMNO o PROFESOR` };
    }

    if (receptor_tipo === 'PROFESOR_JEFE') {
        await verificarProfesorJefe(curso_id, receptor_id, token);
    } else if (receptor_tipo === 'PROFESOR_ASIGNATURA') {
        await verificarProfesorAsignatura(curso_id, receptor_id, token);
    } else if (receptor_tipo === 'APODERADO') {
        await verificarApoderadoDeAlumno(alumno_id, receptor_id, token);
    } else {
        throw { status: 400, message: `Tipo de receptor inválido: ${receptor_tipo}` };
    }

    const [result] = await pool.execute(
        'INSERT INTO conversaciones (iniciador_id, iniciador_tipo, receptor_id, receptor_tipo, alumno_id) VALUES (?, ?, ?, ?, ?)',
        [iniciador_id, iniciador_tipo, receptor_id, receptor_tipo, alumnoIdFinal]
    );

    const [rows] = await pool.execute(
        'SELECT * FROM conversaciones WHERE id = ?',
        [result.insertId]
    );

    return { conversacion: rows[0] ?? {} };
}

async function obtenerPorUsuario(usuarioId, token) {
    const [rows] = await pool.execute(
        'SELECT * FROM conversaciones WHERE iniciador_id = ? OR receptor_id = ? ORDER BY fecha_creacion DESC',
        [usuarioId, usuarioId]
    );

    const lista = await Promise.all((rows ?? []).map(async (c) => {
        const [iniciadorNombre, receptorNombre] = await Promise.all([
            obtenerNombrePerfil(c.iniciador_tipo, c.iniciador_id, token),
            obtenerNombrePerfil(c.receptor_tipo, c.receptor_id, token)
        ]);
        return { ...c, iniciador_nombre: iniciadorNombre, receptor_nombre: receptorNombre };
    }));

    return { lista_conversaciones: lista };
}

module.exports = { crearConversacion, obtenerPorUsuario };
