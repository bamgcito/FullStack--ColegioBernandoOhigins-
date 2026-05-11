export interface Usuario { id: number; rut: string; password: string; nombre: string; apellido: string; rol: 'ADMIN'|'PROFESOR'|'ALUMNO'|'APODERADO'; email?: string; telefono?: string; fechaCreacion: string; }
export interface Alumno extends Usuario { rol: 'ALUMNO'; fechaNacimiento: string; direccion: string; edad: number; fechaMatricula: string; apoderadoId?: number; }
export interface Profesor extends Usuario { rol: 'PROFESOR'; especialidad: string; telefono: string; correoInstitucional: string; fechaContrato: string; }
export interface Apoderado extends Usuario { rol: 'APODERADO'; parentesco: string; alumnosIds: number[]; }
export interface Curso { id: number; nivel: string; letra: string; anio: number; profesorJefeId: number|null; alumnosIds: number[]; }
export interface Asignatura { id: number; nombre: string; }
export interface AsignacionDocente { id: number; profesorId: number; cursoId: number; asignaturaId: number; }
export interface Evaluacion { id: number; nombre: string; fecha: string; descripcion: string; asignacionDocenteId: number; }
export interface Nota { id: number; alumnoId: number; evaluacionId: number; nota: number; }
export interface Asistencia { id: number; alumnoId: number; asignacionDocenteId: number; fecha: string; estado: 'PRESENTE'|'AUSENTE'|'ATRASADO'; }
export interface Anotacion { id: number; alumnoId: number; asignacionDocenteId: number; tipo: 'POSITIVA'|'NEGATIVA'|'NEUTRA'; descripcion: string; fecha: string; }
export interface Mensaje { id: number; conversacionId: number; emisorId: number; contenido: string; fecha: string; leido: boolean; }
export interface Conversacion { id: number; participantesIds: number[]; ultimoMensaje?: string; fechaUltimoMensaje?: string; }

export const USUARIOS_MOCK: Usuario[] = [
  { id:2,  rut:'15234567-8', password:'profe123',  nombre:'Yanfranco', apellido:'Cariqueo',      rol:'PROFESOR', email:'ycariqueo@colegiobohiggins.cl', fechaCreacion:'2024-02-01' },
  { id:3,  rut:'16345678-7', password:'profe123',  nombre:'Pablo',     apellido:'Astroza',       rol:'PROFESOR', email:'pastroza@colegiobohiggins.cl',  fechaCreacion:'2024-02-01' },
  { id:4,  rut:'17456789-6', password:'profe123',  nombre:'Boris',     apellido:'Marciel',       rol:'PROFESOR', email:'bmarciel@colegiobohiggins.cl',  fechaCreacion:'2024-02-01' },
  { id:5,  rut:'20111222-3', password:'alumno123', nombre:'Sofía',     apellido:'Ramírez',       rol:'ALUMNO',   fechaCreacion:'2024-03-01' },
  { id:6,  rut:'20222333-4', password:'alumno123', nombre:'Martín',    apellido:'González',      rol:'ALUMNO',   fechaCreacion:'2024-03-01' },
  { id:7,  rut:'20333444-5', password:'alumno123', nombre:'Valentina', apellido:'Pérez',         rol:'ALUMNO',   fechaCreacion:'2024-03-01' },
  { id:11, rut:'30111222-K', password:'apod123',   nombre:'María',     apellido:'Ramírez',       rol:'APODERADO',fechaCreacion:'2024-03-01' },
  { id:12, rut:'30222333-K', password:'apod123',   nombre:'Jorge',     apellido:'González',      rol:'APODERADO',fechaCreacion:'2024-03-01' },
];

export const PROFESORES_MOCK: Profesor[] = [
  { id:2, rut:'15234567-8', password:'profe123', nombre:'Yanfranco', apellido:'Cariqueo', rol:'PROFESOR', especialidad:'Matemáticas',           telefono:'+56 9 8765 4321', correoInstitucional:'ycariqueo@colegiobohiggins.cl', fechaContrato:'2022-03-01', fechaCreacion:'2024-02-01' },
  { id:3, rut:'16345678-7', password:'profe123', nombre:'Pablo',     apellido:'Astroza',  rol:'PROFESOR', especialidad:'Lenguaje y Comunicación', telefono:'+56 9 7654 3210', correoInstitucional:'pastroza@colegiobohiggins.cl',  fechaContrato:'2021-03-01', fechaCreacion:'2024-02-01' },
  { id:4, rut:'17456789-6', password:'profe123', nombre:'Boris',     apellido:'Marciel',  rol:'PROFESOR', especialidad:'Historia y Geografía',   telefono:'+56 9 6543 2109', correoInstitucional:'bmarciel@colegiobohiggins.cl',  fechaContrato:'2023-03-01', fechaCreacion:'2024-02-01' },
];

export const ALUMNOS_MOCK: Alumno[] = [
  { id:5, rut:'20111222-3', password:'alumno123', nombre:'Sofía',     apellido:'Ramírez', rol:'ALUMNO', fechaNacimiento:'2010-05-12', direccion:'Calle Los Pinos 123, Coquimbo',   edad:14, fechaMatricula:'2024-03-01', apoderadoId:11, fechaCreacion:'2024-03-01' },
  { id:6, rut:'20222333-4', password:'alumno123', nombre:'Martín',    apellido:'González',rol:'ALUMNO', fechaNacimiento:'2010-08-20', direccion:'Av. Del Mar 456, Coquimbo',       edad:14, fechaMatricula:'2024-03-01', apoderadoId:12, fechaCreacion:'2024-03-01' },
  { id:7, rut:'20333444-5', password:'alumno123', nombre:'Valentina', apellido:'Pérez',   rol:'ALUMNO', fechaNacimiento:'2010-11-03', direccion:'Pasaje Las Flores 789, Coquimbo', edad:14, fechaMatricula:'2024-03-01', fechaCreacion:'2024-03-01' },
];

export const APODERADOS_MOCK: Apoderado[] = [
  { id:11, rut:'30111222-K', password:'apod123', nombre:'María', apellido:'Ramírez',  rol:'APODERADO', parentesco:'Madre', alumnosIds:[5], telefono:'+56 9 1111 2222', fechaCreacion:'2024-03-01' },
  { id:12, rut:'30222333-K', password:'apod123', nombre:'Jorge', apellido:'González', rol:'APODERADO', parentesco:'Padre', alumnosIds:[6], telefono:'+56 9 3333 4444', fechaCreacion:'2024-03-01' },
];

export const CURSOS_MOCK: Curso[] = [
  { id:1, nivel:'1°', letra:'A', anio:2025, profesorJefeId:2, alumnosIds:[5,6,7] },
  { id:2, nivel:'1°', letra:'B', anio:2025, profesorJefeId:3, alumnosIds:[] },
  { id:3, nivel:'2°', letra:'A', anio:2025, profesorJefeId:4, alumnosIds:[] },
];

export const ASIGNATURAS_MOCK: Asignatura[] = [
  { id:1, nombre:'Matemáticas' },
  { id:2, nombre:'Lenguaje y Comunicación' },
  { id:3, nombre:'Historia y Geografía' },
  { id:4, nombre:'Ciencias Naturales' },
  { id:5, nombre:'Inglés' },
];

export const ASIGNACIONES_MOCK: AsignacionDocente[] = [
  { id:1, profesorId:2, cursoId:1, asignaturaId:1 },
  { id:2, profesorId:3, cursoId:1, asignaturaId:2 },
  { id:3, profesorId:4, cursoId:1, asignaturaId:3 },
  { id:4, profesorId:2, cursoId:2, asignaturaId:1 },
  { id:5, profesorId:3, cursoId:2, asignaturaId:2 },
];

export const EVALUACIONES_MOCK: Evaluacion[] = [
  { id:1, nombre:'Prueba 1',  fecha:'2025-04-10', descripcion:'Álgebra básica',    asignacionDocenteId:1 },
  { id:2, nombre:'Prueba 2',  fecha:'2025-05-15', descripcion:'Geometría',         asignacionDocenteId:1 },
  { id:3, nombre:'Control 1', fecha:'2025-04-12', descripcion:'Comprensión lectora',asignacionDocenteId:2 },
  { id:4, nombre:'Prueba 1',  fecha:'2025-04-18', descripcion:'Prehistoria',       asignacionDocenteId:3 },
];

export const NOTAS_MOCK: Nota[] = [
  { id:1, alumnoId:5, evaluacionId:1, nota:6.2 },
  { id:2, alumnoId:5, evaluacionId:2, nota:5.8 },
  { id:3, alumnoId:5, evaluacionId:3, nota:6.5 },
  { id:4, alumnoId:5, evaluacionId:4, nota:5.0 },
  { id:5, alumnoId:6, evaluacionId:1, nota:4.5 },
  { id:6, alumnoId:6, evaluacionId:2, nota:5.2 },
  { id:7, alumnoId:7, evaluacionId:1, nota:7.0 },
  { id:8, alumnoId:7, evaluacionId:2, nota:6.8 },
];

export const ASISTENCIA_MOCK: Asistencia[] = [
  { id:1, alumnoId:5, asignacionDocenteId:1, fecha:'2025-05-05', estado:'PRESENTE' },
  { id:2, alumnoId:5, asignacionDocenteId:1, fecha:'2025-05-04', estado:'PRESENTE' },
  { id:3, alumnoId:5, asignacionDocenteId:1, fecha:'2025-05-03', estado:'ATRASADO' },
  { id:4, alumnoId:5, asignacionDocenteId:2, fecha:'2025-05-05', estado:'PRESENTE' },
  { id:5, alumnoId:5, asignacionDocenteId:2, fecha:'2025-05-04', estado:'AUSENTE'  },
  { id:6, alumnoId:6, asignacionDocenteId:1, fecha:'2025-05-05', estado:'AUSENTE'  },
  { id:7, alumnoId:7, asignacionDocenteId:1, fecha:'2025-05-05', estado:'PRESENTE' },
];

export const ANOTACIONES_MOCK: Anotacion[] = [
  { id:1, alumnoId:5, asignacionDocenteId:1, tipo:'POSITIVA', descripcion:'Excelente participación en clase',             fecha:'2025-05-03' },
  { id:2, alumnoId:6, asignacionDocenteId:1, tipo:'NEGATIVA', descripcion:'No trajo el material de trabajo',              fecha:'2025-05-04' },
  { id:3, alumnoId:7, asignacionDocenteId:2, tipo:'POSITIVA', descripcion:'Apoyó a sus compañeros en actividad grupal',   fecha:'2025-05-05' },
  { id:4, alumnoId:5, asignacionDocenteId:2, tipo:'NEUTRA',   descripcion:'Se retiró antes del término de la clase',      fecha:'2025-05-01' },
];

export const CONVERSACIONES_MOCK: Conversacion[] = [
  { id:1, participantesIds:[2,11], ultimoMensaje:'Hola, quería consultar por la nota de Sofía', fechaUltimoMensaje:'2025-05-05T10:30:00' },
  { id:2, participantesIds:[1,2],  ultimoMensaje:'Recuerda registrar las notas antes del viernes', fechaUltimoMensaje:'2025-05-04T14:00:00' },
];

export const MENSAJES_MOCK: Mensaje[] = [
  { id:1, conversacionId:1, emisorId:11, contenido:'Hola profesor, quería consultar por la nota de Sofía en la última prueba', fecha:'2025-05-05T10:30:00', leido:true  },
  { id:2, conversacionId:1, emisorId:2,  contenido:'Hola María, Sofía obtuvo un 6.2. Le fue muy bien.',                        fecha:'2025-05-05T11:00:00', leido:false },
  { id:3, conversacionId:2, emisorId:1,  contenido:'Yanfranco, recuerda registrar las notas antes del viernes',                fecha:'2025-05-04T14:00:00', leido:true  },
  { id:4, conversacionId:2, emisorId:2,  contenido:'Entendido, las subo esta tarde.',                                          fecha:'2025-05-04T14:30:00', leido:true  },
];

export function getNotasByAlumno(id: number): Nota[] { return NOTAS_MOCK.filter(n => n.alumnoId === id); }
export function getAsistenciaByAlumno(id: number): Asistencia[] { return ASISTENCIA_MOCK.filter(a => a.alumnoId === id); }
export function getAnotacionesByAlumno(id: number): Anotacion[] { return ANOTACIONES_MOCK.filter(a => a.alumnoId === id); }
export function getConversacionesByUsuario(id: number): Conversacion[] { return CONVERSACIONES_MOCK.filter(c => c.participantesIds.includes(id)); }

export function calcularPromedioNotas(alumnoId: number): number {
  const notas = getNotasByAlumno(alumnoId);
  if (notas.length === 0) return 0;
  return Math.round((notas.reduce((acc, n) => acc + n.nota, 0) / notas.length) * 10) / 10;
}

export function calcularPorcentajeAsistencia(alumnoId: number): number {
  const registros = getAsistenciaByAlumno(alumnoId);
  if (registros.length === 0) return 0;
  const presentes = registros.filter(r => r.estado !== 'AUSENTE').length;
  return Math.round((presentes / registros.length) * 100);
}

export function getCursoDeAlumno(alumnoId: number): Curso | undefined {
  return CURSOS_MOCK.find(c => c.alumnosIds.includes(alumnoId));
}

export function getProfesorJefeDeCurso(cursoId: number): Profesor | undefined {
  const curso = CURSOS_MOCK.find(c => c.id === cursoId);
  if (!curso?.profesorJefeId) return undefined;
  return PROFESORES_MOCK.find(p => p.id === curso.profesorJefeId);
}