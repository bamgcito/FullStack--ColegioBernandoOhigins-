import { Component, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { IonContent, IonIcon } from '@ionic/angular/standalone';
import { LayoutComponent } from '../../../shared/components/layout/layout.component';
import { addIcons } from 'ionicons';
import { pencilOutline, addOutline, closeOutline } from 'ionicons/icons';
import { ApiService } from '../../../core/services/api.service';
import { forkJoin } from 'rxjs';

@Component({
  selector: 'app-admin-anotaciones',
  standalone: true,
  imports: [CommonModule, FormsModule, IonContent, IonIcon, LayoutComponent],
  templateUrl: './admin-anotaciones.page.html',
  styleUrls: ['./admin-anotaciones.page.scss']
})
export class AdminAnotacionesPage implements OnInit {
  cursos: any[] = [];
  cursoId = 0;
  asignaturasList: any[] = [];
  asignaciones: any[] = [];
  asignacionId = 0;
  anotaciones: any[] = [];
  alumnos: any[] = [];
  alumnoNombres: Record<string, string> = {};
  showModal = false;
  errorMsg = '';
  exitoMsg = '';
  nueva = { rutAlumno: '', descripcion: '', tipo: 'POSITIVA', fecha: new Date().toISOString().split('T')[0] };

  constructor(private api: ApiService) { addIcons({ pencilOutline, addOutline, closeOutline }); }

  ngOnInit() {
    forkJoin({ cursos: this.api.getCursos(), asignaturas: this.api.getAsignaturas() }).subscribe({
      next: ({ cursos, asignaturas }) => { this.cursos = cursos; this.asignaturasList = asignaturas; }
    });
  }

  onCurso() {
    this.asignaciones = []; this.asignacionId = 0; this.anotaciones = []; this.alumnos = [];
    if (!this.cursoId) return;
    forkJoin({
      asignaciones: this.api.getAsignacionesPorCurso(+this.cursoId),
      alumnos: this.api.getAlumnosDeCurso(+this.cursoId)
    }).subscribe({
      next: ({ asignaciones, alumnos }) => {
        this.asignaciones = asignaciones.map((a: any) => ({
          ...a,
          label: this.asignaturasList.find((s: any) => s.id === a.asignaturaId)?.nombre || `Asig. ${a.id}`
        }));
        this.alumnos = alumnos;
        this.alumnoNombres = {};
        alumnos.forEach((al: any) => { this.alumnoNombres[al.rut] = `${al.nombre} ${al.apellido}`; });
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
  badge(t: string) { return { POSITIVA: 'badge-success', NEGATIVA: 'badge-danger', NEUTRA: 'badge-neutral' }[t] || 'badge-neutral'; }

  abrirModal() {
    this.errorMsg = '';
    this.nueva = { rutAlumno: '', descripcion: '', tipo: 'POSITIVA', fecha: new Date().toISOString().split('T')[0] };
    this.showModal = true;
  }

  crear() {
    if (!this.nueva.rutAlumno || !this.nueva.descripcion || !this.asignacionId) return;
    this.errorMsg = '';
    this.api.registrarAnotacion({ ...this.nueva, asignacionDocenteId: +this.asignacionId }).subscribe({
      next: () => {
        this.showModal = false;
        this.nueva = { rutAlumno: '', descripcion: '', tipo: 'POSITIVA', fecha: new Date().toISOString().split('T')[0] };
        this.exitoMsg = 'Anotación registrada correctamente';
        this.onAsignacion();
      },
      error: () => {
        this.errorMsg = 'Error al registrar la anotación';
      }
    });
  }
}
