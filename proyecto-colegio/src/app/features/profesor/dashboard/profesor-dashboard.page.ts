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
  styleUrls: ['./profesor-dashboard.page.scss'],
  host: { class: 'ion-page' }
})
export class ProfesorDashboardPage implements OnInit {
  asignaciones: any[] = [];
  anotaciones: any[] = [];
  usuarios: any[] = [];

  constructor(private auth: AuthService, private api: ApiService) {
    addIcons({ clipboardOutline, pencilOutline });
  }

  ngOnInit() { this.cargar(); }

  cargar() {
    const uid = this.auth.currentUser?.id ?? 0;
    forkJoin({ cursos: this.api.getCursos(), usuarios: this.api.getUsuarios() }).subscribe({
      next: ({ cursos, usuarios }) => {
        this.usuarios = usuarios;
        if (!cursos.length) return;
        forkJoin(cursos.map((c: any) => this.api.getAsignacionesPorCurso(c.id))).subscribe({
          next: (res: any[]) => {
            this.asignaciones = res.flat().filter((a: any) => a.profesorId === uid);
            if (!this.asignaciones.length) return;
            forkJoin(this.asignaciones.map((a: any) => this.api.getAnotacionesPorAsignacion(a.id))).subscribe({
              next: (aRes: any[]) => { this.anotaciones = aRes.flat().slice(0, 4); }
            });
          }
        });
      }
    });
  }

  getAlumno(id: number) {
    const a = this.usuarios.find(u => u.id === id);
    return a ? `${a.nombre} ${a.apellido}` : `Alumno ${id}`;
  }

  badge(t: string) {
    return { POSITIVA: 'badge-success', NEGATIVA: 'badge-danger', NEUTRA: 'badge-neutral' }[t] || 'badge-neutral';
  }
}