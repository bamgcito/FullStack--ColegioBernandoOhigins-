import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';
import { environment } from '../../../environments/environment';

@Injectable({ providedIn: 'root' })
export class ApiService {
    private base = environment.apiUrl;

    constructor(private http: HttpClient) { }

    // ── USUARIOS ─────────────────────────────────────────────────────────
    getUsuarios(): Observable<any[]> { return this.http.get<any[]>(`${this.base}/bff/ms/usuarios`); }
    crearUsuario(data: any): Observable<any> { return this.http.post<any>(`${this.base}/bff/ms/usuarios`, data); }
    getUsuarioPorId(id: number): Observable<any> { return this.http.get<any>(`${this.base}/bff/ms/usuarios/${id}`); }
    eliminarUsuario(id: number): Observable<any> { return this.http.delete<any>(`${this.base}/bff/ms/usuarios/${id}`); }
    getRoles(): Observable<any[]> { return this.http.get<any[]>(`${this.base}/bff/ms/roles`); }
    crearRol(data: any): Observable<any> { return this.http.post<any>(`${this.base}/bff/ms/roles`, data); }

    // ── PERFILES ─────────────────────────────────────────────────────────
    crearPerfilAlumno(data: any): Observable<any> { return this.http.post<any>(`${this.base}/bff/ms/perfiles/alumnos`, data); }
    getPerfilAlumno(usuarioId: number): Observable<any> { return this.http.get<any>(`${this.base}/bff/ms/perfiles/alumnos/${usuarioId}`); }
    getPerfilesAlumnos(): Observable<any[]> { return this.http.get<any[]>(`${this.base}/bff/ms/perfiles/alumnos`); }
    eliminarPerfilAlumno(usuarioId: number): Observable<any> { return this.http.delete<any>(`${this.base}/bff/ms/perfiles/alumnos/${usuarioId}`); }
    getApoderadosDeAlumno(usuarioId: number): Observable<any[]> { return this.http.get<any[]>(`${this.base}/bff/ms/perfiles/alumnos/${usuarioId}/apoderados`); }

    crearPerfilProfesor(data: any): Observable<any> { return this.http.post<any>(`${this.base}/bff/ms/perfiles/profesores`, data); }
    getPerfilProfesor(usuarioId: number): Observable<any> { return this.http.get<any>(`${this.base}/bff/ms/perfiles/profesores/${usuarioId}`); }
    getPerfilesProfesores(): Observable<any[]> { return this.http.get<any[]>(`${this.base}/bff/ms/perfiles/profesores`); }
    eliminarPerfilProfesor(usuarioId: number): Observable<any> { return this.http.delete<any>(`${this.base}/bff/ms/perfiles/profesores/${usuarioId}`); }

    crearPerfilApoderado(data: any): Observable<any> { return this.http.post<any>(`${this.base}/bff/ms/perfiles/apoderados`, data); }
    getPerfilApoderado(usuarioId: number): Observable<any> { return this.http.get<any>(`${this.base}/bff/ms/perfiles/apoderados/${usuarioId}`); }
    getAlumnosDeApoderado(usuarioId: number): Observable<any[]> { return this.http.get<any[]>(`${this.base}/bff/ms/perfiles/apoderados/${usuarioId}/alumnos`); }
    asociarAlumnoApoderado(data: any): Observable<any> { return this.http.post<any>(`${this.base}/bff/ms/perfiles/asociar`, data); }
    eliminarPerfilApoderado(usuarioId: number): Observable<any> { return this.http.delete<any>(`${this.base}/bff/ms/perfiles/apoderados/${usuarioId}`); }

    // ── CURSOS ────────────────────────────────────────────────────────────
    getCursos(): Observable<any[]> { return this.http.get<any[]>(`${this.base}/bff/ms/cursos`); }
    crearCurso(data: any): Observable<any> { return this.http.post<any>(`${this.base}/bff/ms/cursos`, data); }
    getCursoById(id: number): Observable<any> { return this.http.get<any>(`${this.base}/bff/ms/cursos/${id}`); }
    getCursosByProfesor(profesorId: number): Observable<any[]> { return this.http.get<any[]>(`${this.base}/bff/ms/cursos/profesor/${profesorId}`); }
    getCursoDeAlumno(alumnoId: number): Observable<any> { return this.http.get<any>(`${this.base}/bff/ms/cursos/alumno/${alumnoId}`); }
    asignarAlumnoACurso(cursoId: number, data: any): Observable<any> { return this.http.post<any>(`${this.base}/bff/ms/cursos/${cursoId}/alumnos`, data); }
    asignarProfesorJefe(cursoId: number, data: any): Observable<any> { return this.http.post<any>(`${this.base}/bff/ms/cursos/${cursoId}/profesor-jefe`, data); }
    getAlumnosDeCurso(cursoId: number): Observable<any[]> { return this.http.get<any[]>(`${this.base}/bff/ms/cursos/${cursoId}/alumnos`); }

    // ── ASIGNATURAS ───────────────────────────────────────────────────────
    getAsignaturas(): Observable<any[]> { return this.http.get<any[]>(`${this.base}/bff/ms/asignaturas`); }
    crearAsignatura(data: any): Observable<any> { return this.http.post<any>(`${this.base}/bff/ms/asignaturas`, data); }
    getAsignaturaPorId(id: number): Observable<any> { return this.http.get<any>(`${this.base}/bff/ms/asignaturas/${id}`); }

    // ── ASIGNACIONES ──────────────────────────────────────────────────────
    getAsignaciones(): Observable<any[]> { return this.http.get<any[]>(`${this.base}/bff/ms/asignaciones`); }
    crearAsignacion(data: any): Observable<any> { return this.http.post<any>(`${this.base}/bff/ms/asignaciones`, data); }
    getAsignacionesPorCurso(cursoId: number): Observable<any[]> { return this.http.get<any[]>(`${this.base}/bff/ms/asignaciones/curso/${cursoId}`); }
    getAsignacionesPorProfesor(profesorId: number): Observable<any[]> { return this.http.get<any[]>(`${this.base}/bff/ms/asignaciones/profesor/${profesorId}`); }
    getAsignacionPorId(id: number): Observable<any> { return this.http.get<any>(`${this.base}/bff/ms/asignaciones/${id}`); }

    // ── EVALUACIONES ──────────────────────────────────────────────────────
    getEvaluaciones(): Observable<any[]> { return this.http.get<any[]>(`${this.base}/bff/ms/evaluaciones`); }
    crearEvaluacion(data: any): Observable<any> { return this.http.post<any>(`${this.base}/bff/ms/evaluaciones`, data); }
    getEvaluacionesPorAsignacion(asignacionId: number): Observable<any[]> { return this.http.get<any[]>(`${this.base}/bff/ms/evaluaciones/asignacion/${asignacionId}`); }
    getEvaluacionPorId(id: number): Observable<any> { return this.http.get<any>(`${this.base}/bff/ms/evaluaciones/${id}`); }

    // ── NOTAS ─────────────────────────────────────────────────────────────
    crearNota(data: any): Observable<any> { return this.http.post<any>(`${this.base}/bff/ms/notas`, data); }
    getNotasAlumno(alumnoId: number | string): Observable<any[]> { return this.http.get<any[]>(`${this.base}/bff/ms/notas/alumno/${alumnoId}`); }
    getPromedioAlumno(alumnoId: number | string): Observable<any> { return this.http.get<any>(`${this.base}/bff/ms/notas/alumno/${alumnoId}/promedio`); }
    getNotasPorAlumnoYAsignacion(alumnoId: number | string, asignacionId: number): Observable<any[]> { return this.http.get<any[]>(`${this.base}/bff/ms/notas/alumno/${alumnoId}/asignacion/${asignacionId}`); }
    getNotasPorEvaluacion(evaluacionId: number): Observable<any[]> { return this.http.get<any[]>(`${this.base}/bff/ms/notas/evaluacion/${evaluacionId}`); }

    // ── ASISTENCIA ────────────────────────────────────────────────────────
    registrarAsistencia(data: any): Observable<any> { return this.http.post<any>(`${this.base}/bff/ms/asistencia`, data); }
    getAsistenciaAlumno(alumnoId: number | string): Observable<any[]> { return this.http.get<any[]>(`${this.base}/bff/ms/asistencia/alumno/${alumnoId}`); }
    getPorcentajeAsistencia(alumnoId: number | string): Observable<any> { return this.http.get<any>(`${this.base}/bff/ms/asistencia/alumno/${alumnoId}/porcentaje`); }
    getAsistenciaPorAsignacion(asignacionId: number): Observable<any[]> { return this.http.get<any[]>(`${this.base}/bff/ms/asistencia/asignacion/${asignacionId}`); }

    // ── ANOTACIONES ───────────────────────────────────────────────────────
    registrarAnotacion(data: any): Observable<any> { return this.http.post<any>(`${this.base}/bff/ms/anotaciones`, data); }
    getAnotacionesAlumno(alumnoId: number | string): Observable<any[]> { return this.http.get<any[]>(`${this.base}/bff/ms/anotaciones/alumno/${alumnoId}`); }
    getAnotacionesPorAsignacion(asignacionId: number): Observable<any[]> { return this.http.get<any[]>(`${this.base}/bff/ms/anotaciones/asignacion/${asignacionId}`); }

    // ── HORARIOS ──────────────────────────────────────────────────────────
    crearHorario(data: any): Observable<any> { return this.http.post<any>(`${this.base}/bff/ms/horarios`, data); }
    eliminarHorario(id: number): Observable<any> { return this.http.delete<any>(`${this.base}/bff/ms/horarios/${id}`); }
    getHorariosPorCurso(cursoId: number): Observable<any[]> { return this.http.get<any[]>(`${this.base}/bff/ms/horarios/curso/${cursoId}`); }
    getHorariosPorProfesor(profesorId: number): Observable<any[]> { return this.http.get<any[]>(`${this.base}/bff/ms/horarios/profesor/${profesorId}`); }
    getHorariosPorAsignacion(asignacionId: number): Observable<any[]> { return this.http.get<any[]>(`${this.base}/bff/ms/horarios/asignacion/${asignacionId}`); }
}
