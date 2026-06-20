const MS_PERFILES = 'http://ms-perfiles:8087';

async function obtenerNombrePerfil(tipo, id, token) {
    let endpoint;
    if (tipo === 'APODERADO') endpoint = `${MS_PERFILES}/perfiles/apoderados/${id}`;
    else if (tipo === 'ALUMNO') endpoint = `${MS_PERFILES}/perfiles/alumnos/${id}`;
    else endpoint = `${MS_PERFILES}/perfiles/profesores/${id}`;

    try {
        const response = await fetch(endpoint, { headers: { 'Authorization': token } });
        if (!response.ok) return null;
        const texto = await response.text();
        const perfil = texto ? JSON.parse(texto) : null;
        if (!perfil || !perfil.nombre) return null;
        return `${perfil.nombre} ${perfil.apellido || ''}`.trim();
    } catch {
        return null;
    }
}

module.exports = { obtenerNombrePerfil, MS_PERFILES };
