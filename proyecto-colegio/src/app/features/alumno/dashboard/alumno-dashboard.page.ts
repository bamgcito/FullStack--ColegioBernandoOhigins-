import { Component, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { RouterLink } from '@angular/router';
import { IonContent, IonIcon } from '@ionic/angular/standalone';
import { LayoutComponent } from '../../../shared/components/layout/layout.component';
import { addIcons } from 'ionicons';
import { statsChartOutline, checkmarkCircleOutline, documentTextOutline } from 'ionicons/icons';
import { AuthService } from '../../../core/services/auth.service';
import { ApiService } from '../../../core/services/api.service';
import { forkJoin } from 'rxjs';
import { catchError } from 'rxjs/operators';
import { of } from 'rxjs';

@Component({
  selector: 'app-alumno-dashboard',
  standalone: true,
  imports: [CommonModule, RouterLink, IonContent, IonIcon, LayoutComponent],
  templateUrl: './alumno-dashboard.page.html',
  styleUrls: ['./alumno-dashboard.page.scss']
})
export class AlumnoDashboardPage implements OnInit {
  alumno: any = null;
  cursoLabel = '—';
  profeJefeNombre = 'Sin asignar';
  promedio = 0; asistencia = 0; totalNotas = 0;
  anotaciones: any[] = [];
  ultimasNotas: any[] = [];

  constructor(private auth: AuthService, private api: ApiService) {
    addIcons({ statsChartOutline, checkmarkCircleOutline, documentTextOutline });
  }

  ngOnInit() {
    const rut = this.auth.currentUser?.rut ?? '';
    this.alumno = this.auth.currentUser;
    if (!rut) return;

    forkJoin({
      notas: this.api.getNotasAlumno(rut).pipe(catchError(() => of([]))),
      asistencia: this.api.getAsistenciaAlumno(rut).pipe(catchError(() => of([]))),
      anotaciones: this.api.getAnotacionesAlumno(rut).pipe(catchError(() => of([]))),
      promedio: this.api.getPromedioAlumno(rut).pipe(catchError(() => of(null))),
      cursos: this.api.getCursos().pipe(catchError(() => of([])))
    }).subscribe({
      next: ({ notas, asistencia, anotaciones, promedio, cursos }) => {
        this.totalNotas = notas.length;
        this.ultimasNotas = notas.slice(0, 4);
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
  badgeAnotacion(t: string) { return { POSITIVA: 'badge-success', NEGATIVA: 'badge-danger', NEUTRA: 'badge-neutral' }[t] || 'badge-neutral'; }
}