import { Component, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { IonContent, IonIcon } from '@ionic/angular/standalone';
import { LayoutComponent } from '../../../shared/components/layout/layout.component';
import { addIcons } from 'ionicons';
import { checkmarkOutline, closeOutline, timeOutline, saveOutline } from 'ionicons/icons';
import { AuthService } from '../../../core/services/auth.service';
import { ApiService } from '../../../core/services/api.service';
import { forkJoin } from 'rxjs';

@Component({
  selector: 'app-profesor-asistencia',
  standalone: true,
  imports: [CommonModule, FormsModule, IonContent, IonIcon, LayoutComponent],
  templateUrl: './profesor-asistencia.page.html',
  styleUrls: ['./profesor-asistencia.page.scss']
})
export class ProfesorAsistenciaPage implements OnInit {
  asignaciones: any[] = [];
  asignacionIdx = -1;
  alumnos: any[] = [];
  registros: Record<number, string> = {};
  fecha = new Date().toISOString().split('T')[0];
  guardado = false;
  errorMsg = '';

  constructor(private auth: AuthService, private api: ApiService) {
    addIcons({ checkmarkOutline, closeOutline, timeOutline, saveOutline });
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

  seleccionar(idx: number) {
    this.asignacionIdx = idx;
    this.guardado = false;
    this.errorMsg = '';
    this.onAsig();
  }

  onAsig() {
    const asig = this.asignaciones[this.asignacionIdx];
    if (!asig?.cursoId) return;
    this.api.getAlumnosDeCurso(asig.cursoId).subscribe({
      next: alumnos => {
        this.alumnos = alumnos.filter((al: any) => al.nombre);
        this.alumnos.forEach(al => { this.registros[al.alumnoId] = 'PRESENTE'; });
      }
    });
  }

  guardar() {
    const asig = this.asignaciones[this.asignacionIdx];
    if (!asig || !this.alumnos.length) return;
    this.errorMsg = '';
    forkJoin(this.alumnos.map(al => this.api.registrarAsistencia({
      alumnoId: al.alumnoId, asignacionId: asig.id, fecha: this.fecha, estado: this.registros[al.alumnoId] || 'PRESENTE'
    }))).subscribe({
      next: () => {
        this.guardado = true;
        this.errorMsg = '';
      },
      error: () => {
        this.errorMsg = 'Error al registrar la asistencia. Intenta nuevamente.';
      }
    });
  }
}
