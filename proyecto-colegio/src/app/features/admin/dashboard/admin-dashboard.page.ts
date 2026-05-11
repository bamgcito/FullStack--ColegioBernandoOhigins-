import { Component, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { RouterLink } from '@angular/router';
import { IonContent, IonIcon } from '@ionic/angular/standalone';
import { LayoutComponent } from '../../../shared/components/layout/layout.component';
import { addIcons } from 'ionicons';
import { peopleOutline, schoolOutline, personOutline, documentTextOutline, bookOutline } from 'ionicons/icons';
import { ApiService } from '../../../core/services/api.service';
import { forkJoin } from 'rxjs';

@Component({
  selector: 'app-admin-dashboard',
  standalone: true,
  imports: [CommonModule, RouterLink, IonContent, IonIcon, LayoutComponent],
  templateUrl: './admin-dashboard.page.html',
  styleUrls: ['./admin-dashboard.page.scss'],
  host: { class: 'ion-page' }
})
export class AdminDashboardPage implements OnInit {
  cursos: any[] = [];
  profesores: any[] = [];
  alumnos: any[] = [];
  anotaciones: any[] = [];
  stats = { totalAlumnos: 0, totalCursos: 0, totalProfesores: 0, totalEvaluaciones: 0, totalAsignaturas: 0 };
  loading = false;

  constructor(private api: ApiService) {
    addIcons({ peopleOutline, schoolOutline, personOutline, documentTextOutline, bookOutline });
  }

  ngOnInit() { this.cargar(); }

  cargar() {
    this.loading = true;

    // Usuarios — independiente para que no falle con los otros
    this.api.getUsuarios().subscribe({
      next: usuarios => {
        const usuariosAlumno = usuarios.filter((u: any) => (u.nombreRol || u.rol) === 'ALUMNO');
        const usuariosProfesor = usuarios.filter((u: any) => (u.nombreRol || u.rol) === 'PROFESOR');
        this.stats.totalAlumnos = usuariosAlumno.length;
        this.stats.totalProfesores = usuariosProfesor.length;

        // Perfiles completos de profesores
        if (usuariosProfesor.length > 0) {
          forkJoin(usuariosProfesor.slice(0, 10).map((u: any) => this.api.getProfesorPorId(u.id))).subscribe({
            next: (profs: any[]) => { this.profesores = profs.filter(p => p && p.nombre); },
            error: () => { }
          });
        }

        // Perfiles completos de alumnos
        if (usuariosAlumno.length > 0) {
          forkJoin(usuariosAlumno.slice(0, 10).map((u: any) => this.api.getAlumnoPorId(u.id))).subscribe({
            next: (alums: any[]) => {
              this.alumnos = alums.filter(a => a && a.nombre);
              if (this.alumnos.length > 0) {
                this.api.getAnotacionesAlumno(this.alumnos[0].rut).subscribe({
                  next: data => { this.anotaciones = data.slice(0, 5); },
                  error: () => { }
                });
              }
            },
            error: () => { }
          });
        }
      },
      error: () => { console.error('Error cargando usuarios'); }
    });

    // Cursos — independiente
    this.api.getCursos().subscribe({
      next: cursos => { this.cursos = cursos; this.stats.totalCursos = cursos.length; },
      error: () => { this.stats.totalCursos = 0; }
    });

    // Asignaturas — independiente
    this.api.getAsignaturas().subscribe({
      next: asignaturas => { this.stats.totalAsignaturas = asignaturas.length; },
      error: () => { this.stats.totalAsignaturas = 0; }
    });

    this.loading = false;
  }

  getProfesor(id: number | null) {
    if (!id) return 'Sin asignar';
    const p = this.profesores.find(p => p.id === id);
    return p ? `${p.nombre} ${p.apellido}` : '—';
  }

  getAlumno(id: number) {
    const a = this.alumnos.find(a => a.id === id);
    return a ? `${a.nombre} ${a.apellido}` : '—';
  }

  promedio(id: number) { return 0; }
  asistencia(id: number) { return 0; }
  notaClass(n: number) { if (n >= 6) return 'excelente'; if (n >= 5) return 'buena'; if (n >= 4) return 'suficiente'; return 'reprobado'; }
  badgeAnotacion(t: string) { return { POSITIVA: 'badge-success', NEGATIVA: 'badge-danger', NEUTRA: 'badge-neutral' }[t] || 'badge-neutral'; }
}