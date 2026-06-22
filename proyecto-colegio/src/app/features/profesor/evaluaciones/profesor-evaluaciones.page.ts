import { Component, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { IonContent, IonIcon } from '@ionic/angular/standalone';
import { LayoutComponent } from '../../../shared/components/layout/layout.component';
import { addIcons } from 'ionicons';
import { addOutline, closeOutline } from 'ionicons/icons';
import { AuthService } from '../../../core/services/auth.service';
import { ApiService } from '../../../core/services/api.service';
import { forkJoin } from 'rxjs';

@Component({
  selector: 'app-profesor-evaluaciones',
  standalone: true,
  imports: [CommonModule, FormsModule, IonContent, IonIcon, LayoutComponent],
  templateUrl: './profesor-evaluaciones.page.html',
  styleUrls: ['./profesor-evaluaciones.page.scss']
})
export class ProfesorEvaluacionesPage implements OnInit {
  asignaciones: any[] = [];
  asignacionId = 0;
  evaluaciones: any[] = [];
  showModal = false;
  errorMsg = '';
  exitoMsg = '';
  nueva = { titulo: '', fecha: new Date().toISOString().split('T')[0], descripcion: '' };

  constructor(private auth: AuthService, private api: ApiService) {
    addIcons({ addOutline, closeOutline });
  }

  ngOnInit() {
    const uid = this.auth.currentUser?.id ?? 0;
    forkJoin({ cursos: this.api.getCursos(), asignaturas: this.api.getAsignaturas() }).subscribe({
      next: ({ cursos, asignaturas }) => {
        if (!cursos.length) return;
        forkJoin(cursos.map((c: any) => this.api.getAsignacionesPorCurso(c.id))).subscribe({
          next: (res: any[]) => {
            this.asignaciones = res.flat().filter((a: any) => a.profesorId === uid).map((a: any) => {
              const c = cursos.find((c: any) => c.id === a.cursoId);
              const s = asignaturas.find((s: any) => s.id === a.asignaturaId);
              return { ...a, label: `${s?.nombre || '—'} — ${c?.nivel || ''}${c?.letra || ''}` };
            });
          }
        });
      }
    });
  }

  onAsignacion() {
    this.evaluaciones = [];
    this.exitoMsg = '';
    if (!this.asignacionId) return;
    this.api.getEvaluacionesPorAsignacion(+this.asignacionId).subscribe({
      next: data => { this.evaluaciones = data; }
    });
  }

  abrirModal() {
    this.errorMsg = '';
    this.nueva = { titulo: '', fecha: new Date().toISOString().split('T')[0], descripcion: '' };
    this.showModal = true;
  }

  crear() {
    if (!this.nueva.titulo || !this.asignacionId) return;
    this.errorMsg = '';
    this.api.crearEvaluacion({ titulo: this.nueva.titulo, descripcion: this.nueva.descripcion, asignacionId: +this.asignacionId }).subscribe({
      next: (resp: any) => {
        const msg: string = resp?.mensaje || '';
        if (msg.toLowerCase().includes('exitosamente')) {
          this.showModal = false;
          this.nueva = { titulo: '', fecha: new Date().toISOString().split('T')[0], descripcion: '' };
          this.exitoMsg = 'Evaluación creada correctamente';
          this.onAsignacion();
        } else {
          this.errorMsg = msg || 'Error al crear la evaluación';
        }
      },
      error: () => { this.errorMsg = 'Error al crear la evaluación. Intenta nuevamente.'; }
    });
  }
}
