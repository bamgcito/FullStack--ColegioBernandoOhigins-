import { Component, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { IonContent, IonIcon } from '@ionic/angular/standalone';
import { LayoutComponent } from '../../../shared/components/layout/layout.component';
import { addIcons } from 'ionicons';
import { checkmarkCircleOutline, addOutline, closeOutline, saveOutline } from 'ionicons/icons';
import { ApiService } from '../../../core/services/api.service';
import { forkJoin } from 'rxjs';

@Component({
  selector: 'app-admin-asistencia',
  standalone: true,
  imports: [CommonModule, FormsModule, IonContent, IonIcon, LayoutComponent],
  templateUrl: './admin-asistencia.page.html',
  styleUrls: ['./admin-asistencia.page.scss']
})
export class AdminAsistenciaPage implements OnInit {
  cursos: any[] = [];
  cursoId = 0;
  asignaturasList: any[] = [];
  asignaciones: any[] = [];
  asignacionId = 0;
  registros: any[] = [];
  alumnos: any[] = [];
  alumnoNombres: Record<string, string> = {};
  asistenciaReg: Record<number, string> = {};
  fecha = new Date().toISOString().split('T')[0];
  showModal = false;
  guardado = false;
  errorMsg = '';

  constructor(private api: ApiService) { addIcons({ checkmarkCircleOutline, addOutline, closeOutline, saveOutline }); }

  ngOnInit() {
    forkJoin({ cursos: this.api.getCursos(), asignaturas: this.api.getAsignaturas() }).subscribe({
      next: ({ cursos, asignaturas }) => { this.cursos = cursos; this.asignaturasList = asignaturas; }
    });
  }

  onCurso() {
    this.asignaciones = []; this.asignacionId = 0; this.registros = []; this.alumnos = [];
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
    this.registros = [];
    if (!this.asignacionId) return;
    this.api.getAsistenciaPorAsignacion(+this.asignacionId).subscribe({
      next: data => { this.registros = data; }
    });
  }

  getAlumno(rut: string) { return this.alumnoNombres[rut] || rut || '—'; }
  getAsignatura(asignacionId: number) {
    return this.asignaciones.find((a: any) => a.id === asignacionId)?.label || '—';
  }
  badge(e: string) { return { PRESENTE: 'badge-success', AUSENTE: 'badge-danger', ATRASADO: 'badge-warning' }[e] || 'badge-neutral'; }

  abrirModal() {
    this.guardado = false;
    this.errorMsg = '';
    this.asistenciaReg = {};
    this.alumnos.forEach(al => { this.asistenciaReg[al.id] = 'PRESENTE'; });
    this.showModal = true;
  }

  guardar() {
    const asig = this.asignaciones.find((a: any) => a.id === +this.asignacionId);
    if (!asig) return;
    forkJoin(this.alumnos.map(al => this.api.registrarAsistencia({
      alumnoId: al.alumnoId, asignacionId: asig.id, fecha: this.fecha, estado: this.asistenciaReg[al.id] || 'PRESENTE'
    }))).subscribe({
      next: () => {
        this.guardado = true;
        this.errorMsg = '';
        this.showModal = false;
        this.onAsignacion();
      },
      error: () => {
        this.errorMsg = 'Error al registrar la asistencia. Intenta nuevamente.';
      }
    });
  }
}
