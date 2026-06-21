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
    const uid = this.auth.currentUser?.id ?? 0;
    this.alumno = this.auth.currentUser;
    if (!uid) return;

    forkJoin({
      notas: this.api.getNotasAlumno(uid).pipe(catchError(() => of([]))),
      asistencia: this.api.getAsistenciaAlumno(uid).pipe(catchError(() => of([]))),
      anotaciones: this.api.getAnotacionesAlumno(uid).pipe(catchError(() => of([]))),
      promedio: this.api.getPromedioAlumno(uid).pipe(catchError(() => of(null))),
      curso: this.api.getCursoDeAlumno(uid).pipe(catchError(() => of(null)))
    }).subscribe({
      next: ({ notas, asistencia, anotaciones, promedio, curso }) => {
        this.totalNotas = notas.length;
        this.ultimasNotas = notas.slice(0, 4);
        const mapa: Record<number, number[]> = {};
        notas.forEach((n: any) => {
          const aid = n.asignacionId || 0;
          if (!mapa[aid]) mapa[aid] = [];
          mapa[aid].push(n.valor || 0);
        });
        const promedios = Object.values(mapa).map(vals => vals.reduce((s, v) => s + v, 0) / vals.length);
        this.promedio = promedios.length
          ? Math.round(promedios.reduce((s, p) => s + p, 0) / promedios.length * 10) / 10 : 0;
        this.asistencia = asistencia.length
          ? Math.round(asistencia.filter((r: any) => r.estado !== 'AUSENTE').length / asistencia.length * 100)
          : 0;
        this.anotaciones = anotaciones.slice(0, 3);
        if (curso) {
          this.cursoLabel = `${curso.nivel}${curso.letra}`;
          this.profeJefeNombre = curso.nombreProfesorJefe || 'Sin asignar';
        }
      }
    });
  }

  notaClass(n: number) { if (n >= 6) return 'excelente'; if (n >= 5) return 'buena'; if (n >= 4) return 'suficiente'; return 'reprobado'; }
  badgeAnotacion(t: string) { return { POSITIVA: 'badge-success', NEGATIVA: 'badge-danger', NEUTRA: 'badge-neutral' }[t] || 'badge-neutral'; }
}