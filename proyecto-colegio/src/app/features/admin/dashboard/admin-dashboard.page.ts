import { Component, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { RouterLink } from '@angular/router';
import { IonContent, IonIcon } from '@ionic/angular/standalone';
import { LayoutComponent } from '../../../shared/components/layout/layout.component';
import { addIcons } from 'ionicons';
import { peopleOutline, schoolOutline, personOutline, documentTextOutline, bookOutline } from 'ionicons/icons';
import { ApiService } from '../../../core/services/api.service';
import { forkJoin } from 'rxjs';
import { catchError, of } from 'rxjs';

@Component({
  selector: 'app-admin-dashboard',
  standalone: true,
  imports: [CommonModule, RouterLink, IonContent, IonIcon, LayoutComponent],
  templateUrl: './admin-dashboard.page.html',
  styleUrls: ['./admin-dashboard.page.scss']
})
export class AdminDashboardPage implements OnInit {
  cursos: any[] = [];
  profesores: any[] = [];
  alumnos: any[] = [];
  anotaciones: any[] = [];
  stats = { totalAlumnos: 0, totalCursos: 0, totalProfesores: 0, totalEvaluaciones: 0, totalAsignaturas: 0 };
  loading = false;
  promediosMap: { [id: number]: number } = {};
  asistenciasMap: { [id: number]: number } = {};
  alumnoByUsuarioId: { [usuarioId: number]: any } = {};

  constructor(private api: ApiService) {
    addIcons({ peopleOutline, schoolOutline, personOutline, documentTextOutline, bookOutline });
  }

  ngOnInit() { this.cargar(); }

  cargar() {
    this.loading = true;

    // Usuarios â€” independiente para que no falle con los otros
    this.api.getUsuarios().subscribe({
      next: usuarios => {
        const usuariosAlumno = usuarios.filter((u: any) => (u.nombreRol || u.rol) === 'ALUMNO');
        const usuariosProfesor = usuarios.filter((u: any) => (u.nombreRol || u.rol) === 'PROFESOR');
        this.stats.totalAlumnos = usuariosAlumno.length;
        this.stats.totalProfesores = usuariosProfesor.length;

        // Perfiles completos de profesores
        if (usuariosProfesor.length > 0) {
          forkJoin(usuariosProfesor.slice(0, 10).map((u: any) => this.api.getPerfilProfesor(u.id))).subscribe({
            next: (profs: any[]) => { this.profesores = profs.filter(p => p && p.nombre); },
            error: () => { }
          });
        }

        // Perfiles completos de alumnos
        if (usuariosAlumno.length > 0) {
          forkJoin(usuariosAlumno.slice(0, 10).map((u: any) => this.api.getPerfilAlumno(u.id).pipe(catchError(() => of(null))))).subscribe({
            next: (alums: any[]) => {
              this.alumnos = alums.filter(a => a && a.nombre);
              this.alumnoByUsuarioId = {};
              this.alumnos.forEach((a: any) => { this.alumnoByUsuarioId[a.usuarioId] = a; });
              if (this.alumnos.length === 0) return;

              // Anotaciones de todos los alumnos (alumnoId = usuarioId en ms-asistencia)
              forkJoin(this.alumnos.map((a: any) => this.api.getAnotacionesAlumno(a.usuarioId).pipe(catchError(() => of([]))))).subscribe({
                next: (results: any[]) => {
                  const todas = results.flat().sort((x: any, y: any) =>
                    new Date(y.fechaCreacion).getTime() - new Date(x.fechaCreacion).getTime()
                  );
                  this.anotaciones = todas.slice(0, 5);
                }
              });

              // Promedio de notas por alumno (alumnoId = usuarioId en ms-notas)
              forkJoin(this.alumnos.map((a: any) => this.api.getNotasAlumno(a.usuarioId).pipe(catchError(() => of([]))))).subscribe({
                next: (results: any[]) => {
                  this.alumnos.forEach((a: any, i: number) => {
                    const notas = results[i] || [];
                    const vals = notas.map((n: any) => n.valor ?? n.nota ?? 0).filter((v: number) => v > 0);
                    this.promediosMap[a.id] = vals.length > 0
                      ? Math.round((vals.reduce((s: number, v: number) => s + v, 0) / vals.length) * 10) / 10
                      : 0;
                  });
                }
              });

              // Porcentaje de asistencia por alumno (alumnoId = usuarioId en ms-asistencia)
              forkJoin(this.alumnos.map((a: any) => this.api.getAsistenciaAlumno(a.usuarioId).pipe(catchError(() => of([]))))).subscribe({
                next: (results: any[]) => {
                  this.alumnos.forEach((a: any, i: number) => {
                    const registros = results[i] || [];
                    const presentes = registros.filter((r: any) => r.presente === true || r.estado === 'PRESENTE').length;
                    this.asistenciasMap[a.id] = registros.length > 0
                      ? Math.round((presentes / registros.length) * 100)
                      : 0;
                  });
                }
              });
            },
            error: () => { }
          });
        }
      },
      error: () => { console.error('Error cargando usuarios'); }
    });

    // Cursos â€” independiente
    this.api.getCursos().subscribe({
      next: cursos => { this.cursos = cursos; this.stats.totalCursos = cursos.length; },
      error: () => { this.stats.totalCursos = 0; }
    });

    // Asignaturas â€” independiente
    this.api.getAsignaturas().subscribe({
      next: asignaturas => { this.stats.totalAsignaturas = asignaturas.length; },
      error: () => { this.stats.totalAsignaturas = 0; }
    });

    this.loading = false;
  }

  getProfesor(id: number | null) {
    if (!id) return 'Sin asignar';
    const p = this.profesores.find(p => p.id === id);
    return p ? `${p.nombre} ${p.apellido}` : 'â€”';
  }

  getAlumno(id: number) {
    const a = this.alumnos.find(a => a.id === id);
    return a ? `${a.nombre} ${a.apellido}` : 'â€”';
  }

  getAlumnoByRut(rut: string) {
    const a = this.alumnos.find(a => a.rut === rut);
    return a ? `${a.nombre} ${a.apellido}` : rut || '—';
  }

  getAlumnoById(usuarioId: number) {
    const a = this.alumnoByUsuarioId[usuarioId];
    return a ? `${a.nombre} ${a.apellido}` : `Alumno ${usuarioId}`;
  }

  promedio(id: number) { return this.promediosMap[id] ?? 0; }
  asistencia(id: number) { return this.asistenciasMap[id] ?? 0; }
  notaClass(n: number) { if (n >= 6) return 'excelente'; if (n >= 5) return 'buena'; if (n >= 4) return 'suficiente'; return 'reprobado'; }
  badgeAnotacion(t: string) { return { POSITIVA: 'badge-success', NEGATIVA: 'badge-danger', NEUTRA: 'badge-neutral' }[t] || 'badge-neutral'; }
}