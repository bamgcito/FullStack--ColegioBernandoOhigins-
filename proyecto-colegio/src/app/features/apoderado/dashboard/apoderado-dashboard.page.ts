import { Component, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { RouterLink } from '@angular/router';
import { IonContent, IonIcon } from '@ionic/angular/standalone';
import { LayoutComponent } from '../../../shared/components/layout/layout.component';
import { addIcons } from 'ionicons';
import { statsChartOutline, checkmarkCircleOutline, pencilOutline, chevronDownOutline, chatbubblesOutline } from 'ionicons/icons';
import { AuthService } from '../../../core/services/auth.service';
import { ApiService } from '../../../core/services/api.service';
import { ApoderadoStateService } from '../../../core/services/apoderado-state.service';
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

  constructor(private auth: AuthService, private api: ApiService, private state: ApoderadoStateService) {
    addIcons({ statsChartOutline, checkmarkCircleOutline, pencilOutline, chevronDownOutline, chatbubblesOutline });
  }

  ngOnInit() {
    const apoderadoId = this.auth.currentUser?.id ?? 0;
    if (!apoderadoId) return;

    // Obtiene perfiles de alumnos del apoderado usando usuarioId del apoderado
    this.api.getAlumnosDeApoderado(apoderadoId).pipe(catchError(() => of([]))).subscribe({
      next: (alumnos: any[]) => {
        this.alumnos = alumnos || [];
        if (!this.alumnos.length) return;
        const yaSeleccionado = this.state.pupilo;
        const inicial = yaSeleccionado
          ? (this.alumnos.find(a => a.usuarioId === yaSeleccionado.usuarioId) ?? this.alumnos[0])
          : this.alumnos[0];
        this.seleccionarAlumno(inicial);
      }
    });
  }

  seleccionarAlumno(alumno: any) {
    this.pupilo = alumno;
    this.state.pupilo = alumno;
    this.showSelector = false;
    this.cursoLabel = '—';
    this.profeJefeNombre = 'Sin asignar';
    this.promedio = 0;
    this.asistencia = 0;
    this.anotaciones = [];
    this.notas = [];
    // alumno.usuarioId es el usuarioId de ms-usuarios, que es el ID que usan notas/asistencia/anotaciones/cursos
    this.cargarDatos(alumno.usuarioId);
  }

  private cargarDatos(alumnoId: number) {
    forkJoin({
      notas: this.api.getNotasAlumno(alumnoId).pipe(catchError(() => of([]))),
      asistencia: this.api.getAsistenciaAlumno(alumnoId).pipe(catchError(() => of([]))),
      anotaciones: this.api.getAnotacionesAlumno(alumnoId).pipe(catchError(() => of([]))),
      curso: this.api.getCursoDeAlumno(alumnoId).pipe(catchError(() => of(null)))
    }).subscribe({
      next: ({ notas, asistencia, anotaciones, curso }) => {
        this.notas = (notas || []).slice(0, 4);

        const mapa: Record<number, number[]> = {};
        (notas || []).forEach((n: any) => {
          const aid = n.asignacionId || 0;
          if (!mapa[aid]) mapa[aid] = [];
          mapa[aid].push(n.valor || 0);
        });
        const promedios = Object.values(mapa).map(vals => vals.reduce((s, v) => s + v, 0) / vals.length);
        this.promedio = promedios.length
          ? Math.round(promedios.reduce((s, p) => s + p, 0) / promedios.length * 10) / 10 : 0;

        const registros = asistencia || [];
        this.asistencia = registros.length
          ? Math.round(registros.filter((r: any) => r.estado !== 'AUSENTE').length / registros.length * 100)
          : 0;

        this.anotaciones = (anotaciones || []).slice(0, 3);

        if (curso) {
          this.cursoLabel = `${curso.nivel}${curso.letra}`;
          this.profeJefeNombre = curso.nombreProfesorJefe || 'Sin asignar';
        }
      }
    });
  }

  notaClass(n: number) { if (n >= 6) return 'excelente'; if (n >= 5) return 'buena'; if (n >= 4) return 'suficiente'; return 'reprobado'; }
  badge(t: string) { return { POSITIVA: 'badge-success', NEGATIVA: 'badge-danger', NEUTRA: 'badge-neutral' }[t] || 'badge-neutral'; }
}
