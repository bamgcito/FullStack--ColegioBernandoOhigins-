import { Component, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { RouterLink } from '@angular/router';
import { IonContent, IonIcon } from '@ionic/angular/standalone';
import { LayoutComponent } from '../../../shared/components/layout/layout.component';
import { addIcons } from 'ionicons';
import { statsChartOutline, checkmarkCircleOutline, pencilOutline, chevronDownOutline } from 'ionicons/icons';
import { AuthService } from '../../../core/services/auth.service';
import { ApiService } from '../../../core/services/api.service';
import { forkJoin } from 'rxjs';
import { catchError } from 'rxjs/operators';
import { of } from 'rxjs';

@Component({
  selector: 'app-apoderado-dashboard',
  standalone: true,
  imports: [CommonModule, RouterLink, IonContent, IonIcon, LayoutComponent],
  templateUrl: './apoderado-dashboard.page.html',
  styleUrls: ['./apoderado-dashboard.page.scss']
})
export class ApoderadoDashboardPage implements OnInit {
  alumnos: any[] = [];
  pupilo: any = null;
  cursoLabel = '—'; profeJefeNombre = 'Sin asignar';
  promedio = 0; asistencia = 0;
  anotaciones: any[] = []; notas: any[] = [];
  showSelector = false;

  constructor(private auth: AuthService, private api: ApiService) {
    addIcons({ statsChartOutline, checkmarkCircleOutline, pencilOutline, chevronDownOutline });
  }

  ngOnInit() {
    const apoderadoId = this.auth.currentUser?.id ?? 0;
    if (!apoderadoId) return;

    this.api.getAlumnosDeApoderado(apoderadoId).pipe(catchError(() => of([]))).subscribe({
      next: (alumnos: any[]) => {
        this.alumnos = alumnos || [];
        if (!this.alumnos.length) return;
        this.seleccionarAlumno(this.alumnos[0]);
      }
    });
  }

  seleccionarAlumno(alumno: any) {
    this.pupilo = alumno;
    this.showSelector = false;
    this.cursoLabel = '—';
    this.profeJefeNombre = 'Sin asignar';
    this.promedio = 0;
    this.asistencia = 0;
    this.anotaciones = [];
    this.notas = [];
    this.cargarDatos(alumno.rut);
  }

  private cargarDatos(rut: string) {
    forkJoin({
      notas: this.api.getNotasAlumno(rut).pipe(catchError(() => of([]))),
      asistencia: this.api.getAsistenciaAlumno(rut).pipe(catchError(() => of([]))),
      anotaciones: this.api.getAnotacionesAlumno(rut).pipe(catchError(() => of([]))),
      promedio: this.api.getPromedioAlumno(rut).pipe(catchError(() => of(null))),
      cursos: this.api.getCursos().pipe(catchError(() => of([])))
    }).subscribe({
      next: ({ notas, asistencia, anotaciones, promedio, cursos }) => {
        this.notas = notas.slice(0, 4);
        this.promedio = typeof promedio === 'number' ? promedio : (promedio?.promedioGeneral ?? 0);
        this.asistencia = asistencia.length
          ? Math.round(asistencia.filter((r: any) => r.estado !== 'AUSENTE').length / asistencia.length * 100)
          : 0;
        this.anotaciones = anotaciones.slice(0, 3);
        const miCurso = cursos.find((c: any) => c.alumnos?.some((a: any) => a.rut === rut));
        if (miCurso) {
          this.cursoLabel = `${miCurso.nivel}${miCurso.letra}`;
          this.profeJefeNombre = miCurso.nombreProfesorJefe || 'Sin asignar';
        }
      }
    });
  }

  notaClass(n: number) { if (n >= 6) return 'excelente'; if (n >= 5) return 'buena'; if (n >= 4) return 'suficiente'; return 'reprobado'; }
  badge(t: string) { return { POSITIVA: 'badge-success', NEGATIVA: 'badge-danger', NEUTRA: 'badge-neutral' }[t] || 'badge-neutral'; }
}
