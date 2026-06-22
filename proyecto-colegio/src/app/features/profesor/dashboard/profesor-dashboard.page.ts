import { Component, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { RouterLink } from '@angular/router';
import { IonContent, IonIcon } from '@ionic/angular/standalone';
import { LayoutComponent } from '../../../shared/components/layout/layout.component';
import { addIcons } from 'ionicons';
import { clipboardOutline, pencilOutline, chatbubblesOutline } from 'ionicons/icons';
import { AuthService } from '../../../core/services/auth.service';
import { ApiService } from '../../../core/services/api.service';
import { forkJoin } from 'rxjs';
import { catchError } from 'rxjs/operators';
import { of } from 'rxjs';

@Component({
  selector: 'app-profesor-dashboard',
  standalone: true,
  imports: [CommonModule, RouterLink, IonContent, IonIcon, LayoutComponent],
  templateUrl: './profesor-dashboard.page.html',
  styleUrls: ['./profesor-dashboard.page.scss']
})
export class ProfesorDashboardPage implements OnInit {
  asignaciones: any[] = [];
  anotaciones: any[] = [];

  constructor(private auth: AuthService, private api: ApiService) {
    addIcons({ clipboardOutline, pencilOutline, chatbubblesOutline });
  }

  ngOnInit() { this.cargar(); }

  cargar() {
    const uid = this.auth.currentUser?.id ?? 0;
    if (!uid) return;

    forkJoin({
      asignaciones: this.api.getAsignacionesPorProfesor(uid).pipe(catchError(() => of([]))),
      asignaturas: this.api.getAsignaturas().pipe(catchError(() => of([]))),
      cursos: this.api.getCursos().pipe(catchError(() => of([]))),
      alumnos: this.api.getPerfilesAlumnos().pipe(catchError(() => of([])))
    }).subscribe({
      next: ({ asignaciones, asignaturas, cursos, alumnos }) => {
        this.asignaciones = (asignaciones || []).map((a: any) => {
          const curso = (cursos as any[]).find((c: any) => c.id === a.cursoId);
          const asig = (asignaturas as any[]).find((s: any) => s.id === a.asignaturaId);
          return {
            ...a,
            asignatura: asig?.nombre || '—',
            cursoNombre: curso ? `${curso.nivel}${curso.letra}` : '—'
          };
        });

        if (!this.asignaciones.length) return;

        forkJoin(
          this.asignaciones.map((a: any) =>
            this.api.getAnotacionesPorAsignacion(a.id).pipe(catchError(() => of([])))
          )
        ).subscribe({
          next: (aRes: any[]) => {
            this.anotaciones = aRes.flat().slice(0, 4).map((an: any) => {
              const perfil = (alumnos as any[]).find((p: any) => p.usuarioId === an.alumnoId);
              return { ...an, alumnoNombre: perfil ? `${perfil.nombre} ${perfil.apellido}` : `ID ${an.alumnoId}` };
            });
          }
        });
      }
    });
  }

  badge(t: string) {
    return { POSITIVA: 'badge-success', NEGATIVA: 'badge-danger', NEUTRA: 'badge-neutral' }[t] || 'badge-neutral';
  }
}
