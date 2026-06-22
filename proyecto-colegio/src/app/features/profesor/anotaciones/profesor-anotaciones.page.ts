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
  selector: 'app-profesor-anotaciones',
  standalone: true,
  imports: [CommonModule, FormsModule, IonContent, IonIcon, LayoutComponent],
  templateUrl: './profesor-anotaciones.page.html',
  styleUrls: ['./profesor-anotaciones.page.scss']
})
export class ProfesorAnotacionesPage implements OnInit {
  cursos: any[] = [];
  cursoId = 0;
  asignaturasList: any[] = [];
  asignaciones: any[] = [];
  asignacionId = 0;
  anotaciones: any[] = [];
  alumnos: any[] = [];
  alumnoNombres: Record<string, string> = {};
  alumnoNombresPorId: Record<number, string> = {};
  showModal = false;
  errorMsg = '';
  exitoMsg = '';
  nueva = { alumnoId: 0, descripcion: '', tipo: 'POSITIVA', fecha: new Date().toISOString().split('T')[0] };

  constructor(private auth: AuthService, private api: ApiService) { addIcons({ addOutline, closeOutline }); }

  ngOnInit() {
    const uid = this.auth.currentUser?.id ?? 0;
    forkJoin({ cursos: this.api.getCursos(), asignaturas: this.api.getAsignaturas() }).subscribe({
      next: ({ cursos, asignaturas }) => {
        this.asignaturasList = asignaturas;
        if (!cursos.length) return;
        forkJoin(cursos.map((c: any) => this.api.getAsignacionesPorCurso(c.id))).subscribe({
          next: (res: any[]) => {
            const misCursoIds = new Set(
              res.flat().filter((a: any) => a.profesorId === uid).map((a: any) => a.cursoId)
            );
            this.cursos = cursos.filter((c: any) => misCursoIds.has(c.id));
          }
        });
      }
    });
  }

  onCurso() {
    this.asignaciones = []; this.asignacionId = 0; this.anotaciones = []; this.alumnos = []; this.alumnoNombres = {};
    if (!this.cursoId) return;
    const uid = this.auth.currentUser?.id ?? 0;
    forkJoin({
      asignaciones: this.api.getAsignacionesPorCurso(+this.cursoId),
      alumnos: this.api.getAlumnosDeCurso(+this.cursoId)
    }).subscribe({
      next: ({ asignaciones, alumnos }) => {
        this.asignaciones = asignaciones
          .filter((a: any) => a.profesorId === uid)
          .map((a: any) => ({
            ...a,
            label: this.asignaturasList.find((s: any) => s.id === a.asignaturaId)?.nombre || `Asig. ${a.id}`
          }));
        this.alumnos = alumnos;
        alumnos.forEach((al: any) => {
          this.alumnoNombres[al.rut] = `${al.nombre} ${al.apellido}`;
          this.alumnoNombresPorId[al.alumnoId] = `${al.nombre} ${al.apellido}`;
        });
      }
    });
  }

  onAsignacion() {
    this.anotaciones = [];
    this.exitoMsg = '';
    if (!this.asignacionId) return;
    this.api.getAnotacionesPorAsignacion(+this.asignacionId).subscribe({
      next: data => { this.anotaciones = data; }
    });
  }

  getAlumno(rut: string) { return this.alumnoNombres[rut] || rut || '—'; }
  getAlumnoById(id: number) { return this.alumnoNombresPorId[id] || String(id) || '—'; }
  badge(t: string) { return { POSITIVA: 'badge-success', NEGATIVA: 'badge-danger', NEUTRA: 'badge-neutral' }[t] || 'badge-neutral'; }

  abrirModal() {
    this.errorMsg = '';
    this.nueva = { alumnoId: 0, descripcion: '', tipo: 'POSITIVA', fecha: new Date().toISOString().split('T')[0] };
    this.showModal = true;
  }

  crear() {
    if (!this.nueva.alumnoId || !this.nueva.descripcion || !this.asignacionId) return;
    this.errorMsg = '';
    this.api.registrarAnotacion({ alumnoId: this.nueva.alumnoId, descripcion: this.nueva.descripcion, tipo: this.nueva.tipo, asignacionId: +this.asignacionId }).subscribe({
      next: () => {
        this.showModal = false;
        this.nueva = { alumnoId: 0, descripcion: '', tipo: 'POSITIVA', fecha: new Date().toISOString().split('T')[0] };
        this.exitoMsg = 'Anotación registrada correctamente';
        this.onAsignacion();
      },
      error: () => {
        this.errorMsg = 'Error al registrar la anotación';
      }
    });
  }
}
