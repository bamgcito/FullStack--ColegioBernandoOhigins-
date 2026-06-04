import { Injectable } from '@angular/core';
import { HttpClient, HttpParams } from '@angular/common/http';
import { Observable } from 'rxjs';
import { environment } from '../../../environments/environment';

@Injectable({ providedIn: 'root' })
export class ApiService {
    private base = `${environment.apiUrl}/bff`;

    constructor(private http: HttpClient) { }

    // USUARIOS
    getUsuarios(): Observable<any[]> { return this.http.get<any[]>(`${this.base}/usuarios`); }

    crearUsuario(data: any, responseType: 'json' | 'text' = 'json'): Observable<any> {
        return this.http.post(`${this.base}/usuarios`, data, { responseType } as any);
    }
    crearAlumno(data: any, responseType: 'json' | 'text' = 'json'): Observable<any> {
        return this.http.post(`${this.base}/usuarios/alumnos`, data, { responseType } as any);
    }
    crearProfesor(data: any, responseType: 'json' | 'text' = 'json'): Observable<any> {
        return this.http.post(`${this.base}/usuarios/profesores`, data, { responseType } as any);
    }
    crearApoderado(data: any, responseType: 'json' | 'text' = 'json'): Observable<any> {
        return this.http.post(`${this.base}/usuarios/apoderados`, data, { responseType } as any);
    }
    asociarApoderado(data: any, responseType: 'json' | 'text' = 'json'): Observable<any> {
        return this.http.post(`${this.base}/usuarios/alumnos/apoderados`, data, { responseType } as any);
    }

    getInfoAlumno(rut: string): Observable<any> { return this.http.get<any>(`${this.base}/usuarios/alumnos/${rut}/info`); }
    getAlumnoPorId(id: number): Observable<any> { return this.http.get<any>(`${this.base}/usuarios/alumnos/id/${id}`); }
    getProfesorPorId(id: number): Observable<any> { return this.http.get<any>(`${this.base}/usuarios/profesores/${id}`); }
    eliminarUsuario(id: number): Observable<any> { return this.http.delete<any>(`${this.base}/usuarios/${id}`); }
    getAlumnosDeApoderado(id: number): Observable<any[]> { return this.http.get<any[]>(`${this.base}/usuarios/apoderados/${id}/alumnos`); }

    // CURSOS
    getCursos(): Observable<any[]> { return this.http.get<any[]>(`${this.base}/cursos`); }
    crearCurso(data: any, responseType: 'json' | 'text' = 'json'): Observable<any> {
        return this.http.post(`${this.base}/cursos`, data, { responseType } as any);
    }
    asignarProfesorJefe(cursoId: number, data: any): Observable<any> {
        return this.http.put<any>(`${this.base}/cursos/${cursoId}/profesor-jefe`, data);
    }
    getCursoById(id: number): Observable<any> { return this.http.get<any>(`${this.base}/cursos/${id}`); }
    getAlumnosDeCurso(id: number): Observable<any[]> { return this.http.get<any[]>(`${this.base}/cursos/${id}/alumnos`); }
    getCursosByProfesor(profesorId: number): Observable<any[]> { return this.http.get<any[]>(`${this.base}/cursos/profesor/${profesorId}`); }
    asignarAlumnoACurso(data: any): Observable<any> { return this.http.post<any>(`${this.base}/cursos/alumnos`, data); }

    // ASIGNATURAS
    getAsignaturas(): Observable<any[]> { return this.http.get<any[]>(`${this.base}/academico/asignaturas`); }
    crearAsignatura(data: any, responseType: 'json' | 'text' = 'json'): Observable<any> {
        return this.http.post(`${this.base}/academico/asignaturas`, data, { responseType } as any);
    }

    // ASIGNACIONES
    getAsignacionesPorCurso(cursoId: number): Observable<any[]> { return this.http.get<any[]>(`${this.base}/academico/asignaciones/curso/${cursoId}`); }
    getAsignacionPorId(id: number): Observable<any> { return this.http.get<any>(`${this.base}/academico/asignaciones/${id}`); }
    crearAsignacion(data: any, responseType: 'json' | 'text' = 'json'): Observable<any> {
        return this.http.post(`${this.base}/academico/asignaciones`, data, { responseType } as any);
    }

    // EVALUACIONES
    getEvaluacionesPorAsignacion(asignacionId: number): Observable<any[]> { return this.http.get<any[]>(`${this.base}/academico/evaluaciones/asignacion/${asignacionId}`); }
    crearEvaluacion(data: any, responseType: 'json' | 'text' = 'json'): Observable<any> {
        return this.http.post(`${this.base}/academico/evaluaciones`, data, { responseType } as any);
    }

    // NOTAS
    getNotasAlumno(rutAlumno: string): Observable<any[]> {
        const p = new HttpParams().set('rutAlumno', rutAlumno);
        return this.http.get<any[]>(`${this.base}/academico/notas/alumno`, { params: p });
    }
    getPromedioAlumno(rutAlumno: string): Observable<any> {
        const p = new HttpParams().set('rutAlumno', rutAlumno);
        return this.http.get<any>(`${this.base}/academico/notas/alumno/promedio`, { params: p });
    }
    getNotasPorEvaluacion(evaluacionId: number): Observable<any[]> { return this.http.get<any[]>(`${this.base}/academico/notas/evaluacion/${evaluacionId}`); }
    crearNota(data: any, responseType: 'json' | 'text' = 'json'): Observable<any> {
        return this.http.post(`${this.base}/academico/notas`, data, { responseType } as any);
    }

    // ASISTENCIA
    getAsistenciaAlumno(rut: string): Observable<any[]> { return this.http.get<any[]>(`${this.base}/asistencia/alumno/${rut}`); }
    getPorcentajeAsistencia(rut: string): Observable<any> { return this.http.get<any>(`${this.base}/asistencia/alumno/${rut}/porcentaje`); }
    getAsistenciaPorAsignacion(asignacionId: number): Observable<any[]> { return this.http.get<any[]>(`${this.base}/asistencia/asignacion/${asignacionId}`); }
    registrarAsistencia(data: any, responseType: 'json' | 'text' = 'json'): Observable<any> {
        return this.http.post(`${this.base}/asistencia`, data, { responseType } as any);
    }

    // ANOTACIONES
    getAnotacionesAlumno(rut: string): Observable<any[]> { return this.http.get<any[]>(`${this.base}/asistencia/anotaciones/alumno/${rut}`); }
    getAnotacionesPorAsignacion(asignacionId: number): Observable<any[]> { return this.http.get<any[]>(`${this.base}/asistencia/anotaciones/asignacion/${asignacionId}`); }
    registrarAnotacion(data: any, responseType: 'json' | 'text' = 'json'): Observable<any> {
        return this.http.post(`${this.base}/asistencia/anotaciones`, data, { responseType } as any);
    }
}