import { Component, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { RouterLink } from '@angular/router';
import { IonContent, IonIcon } from '@ionic/angular/standalone';
import { LayoutComponent } from '../../../shared/components/layout/layout.component';
import { addIcons } from 'ionicons';
import { clipboardOutline, pencilOutline } from 'ionicons/icons';
import { AuthService } from '../../../core/services/auth.service';
import { ApiService } from '../../../core/services/api.service';
import { forkJoin } from 'rxjs';

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
    addIcons({ clipboardOutline, pencilOutline });
  }

  ngOnInit() { this.cargar(); }

  cargar() {
    const uid = this.auth.currentUser?.id ?? 0;
    forkJoin({ cursos: this.api.getCursos(), asignaturas: this.api.getAsignaturas() }).subscribe({
      next: ({ cursos, asignaturas }) => {
        if (!cursos.length) return;
        forkJoin(cursos.map((c: any) => this.api.getAsignacionesPorCurso(c.id))).subscribe({
          next: (res: any[]) => {
            this.asignaciones = res.flat().filter((a: any) => a.profesorId === uid).map((a: any) => {
              const c = cursos.find((c: any) => c.id === a.cursoId);
              const s = asignaturas.find((s: any) => s.id === a.asignaturaId);
              return { ...a, asignatura: s?.nombre || 'â€”', cursoNombre: c ? `${c.nivel}${c.letra}` : 'â€”' };
            });
            if (!this.asignaciones.length) return;
            forkJoin(this.asignaciones.map((a: any) => this.api.getAnotacionesPorAsignacion(a.id))).subscribe({
              next: (aRes: any[]) => { this.anotaciones = aRes.flat().slice(0, 4); }
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
