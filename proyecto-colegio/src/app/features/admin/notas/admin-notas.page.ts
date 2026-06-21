import { Component, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { IonContent, IonIcon } from '@ionic/angular/standalone';
import { LayoutComponent } from '../../../shared/components/layout/layout.component';
import { addIcons } from 'ionicons';
import { starOutline, addOutline, closeOutline, saveOutline } from 'ionicons/icons';
import { ApiService } from '../../../core/services/api.service';
import { forkJoin } from 'rxjs';

@Component({
  selector: 'app-admin-notas',
  standalone: true,
  imports: [CommonModule, FormsModule, IonContent, IonIcon, LayoutComponent],
  templateUrl: './admin-notas.page.html',
  styleUrls: ['./admin-notas.page.scss']
})
export class AdminNotasPage implements OnInit {
  cursos: any[] = [];
  cursoId = 0;
  asignaturasList: any[] = [];
  asignaciones: any[] = [];
  asignacionId = 0;
  evaluaciones: any[] = [];
  evaluacionId = 0;
  notas: any[] = [];
  alumnos: any[] = [];
  alumnoNombres: Record<string, string> = {};
  notasReg: Record<number, number> = {};
  showModal = false;
  guardado = false;
  errorMsg = '';

  constructor(private api: ApiService) { addIcons({ starOutline, addOutline, closeOutline, saveOutline }); }

  ngOnInit() {
    forkJoin({ cursos: this.api.getCursos(), asignaturas: this.api.getAsignaturas() }).subscribe({
      next: ({ cursos, asignaturas }) => { this.cursos = cursos; this.asignaturasList = asignaturas; }
    });
  }

  onCurso() {
    this.asignaciones = []; this.asignacionId = 0; this.evaluaciones = []; this.evaluacionId = 0;
    this.alumnos = []; this.notas = [];
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
    this.evaluaciones = []; this.evaluacionId = 0; this.notas = [];
    if (!this.asignacionId) return;
    this.api.getEvaluacionesPorAsignacion(+this.asignacionId).subscribe({
      next: data => { this.evaluaciones = data; }
    });
  }

  onEvaluacion() {
    this.notas = [];
    if (!this.evaluacionId) return;
    this.api.getNotasPorEvaluacion(+this.evaluacionId).subscribe({
      next: data => { this.notas = data; }
    });
  }

  getAlumno(rut: string) { return this.alumnoNombres[rut] || rut || '—'; }
  getEvaluacion(id: number) { return this.evaluaciones.find((e: any) => e.id === id)?.nombre || '—'; }
  notaClass(n: number) { if (n >= 6) return 'excelente'; if (n >= 5) return 'buena'; if (n >= 4) return 'suficiente'; return 'reprobado'; }

  abrirModal() {
    this.guardado = false;
    this.errorMsg = '';
    this.notasReg = {};
    this.alumnos.forEach(al => { this.notasReg[al.id] = 0; });
    this.showModal = true;
  }

  guardar() {
    const reqs = this.alumnos.filter(al => this.notasReg[al.id] > 0)
      .map(al => this.api.crearNota({ alumnoId: al.alumnoId, evaluacionId: +this.evaluacionId, valor: this.notasReg[al.id] }));
    if (!reqs.length) { this.guardado = true; return; }
    forkJoin(reqs).subscribe({
      next: () => {
        this.guardado = true;
        this.errorMsg = '';
        this.showModal = false;
        this.onEvaluacion();
      },
      error: () => {
        this.errorMsg = 'Error al guardar las notas. Intenta nuevamente.';
      }
    });
  }
}
