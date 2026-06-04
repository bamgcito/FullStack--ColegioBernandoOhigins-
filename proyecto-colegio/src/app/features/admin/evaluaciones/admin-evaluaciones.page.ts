import { Component, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { IonContent, IonIcon } from '@ionic/angular/standalone';
import { LayoutComponent } from '../../../shared/components/layout/layout.component';
import { addIcons } from 'ionicons';
import { documentTextOutline, addOutline, closeOutline } from 'ionicons/icons';
import { ApiService } from '../../../core/services/api.service';
import { forkJoin } from 'rxjs';

@Component({
  selector: 'app-admin-evaluaciones',
  standalone: true,
  imports: [CommonModule, FormsModule, IonContent, IonIcon, LayoutComponent],
  templateUrl: './admin-evaluaciones.page.html',
  styleUrls: ['./admin-evaluaciones.page.scss']
})
export class AdminEvaluacionesPage implements OnInit {
  cursos: any[] = [];
  cursoId = 0;
  asignaturasList: any[] = [];
  asignaciones: any[] = [];
  asignacionId = 0;
  evaluaciones: any[] = [];
  showModal = false;
  errorMsg = '';
  exitoMsg = '';
  nueva = { nombre: '', fecha: new Date().toISOString().split('T')[0], descripcion: '' };

  constructor(private api: ApiService) { addIcons({ documentTextOutline, addOutline, closeOutline }); }

  ngOnInit() {
    forkJoin({ cursos: this.api.getCursos(), asignaturas: this.api.getAsignaturas() }).subscribe({
      next: ({ cursos, asignaturas }) => { this.cursos = cursos; this.asignaturasList = asignaturas; }
    });
  }

  onCurso() {
    this.asignaciones = []; this.asignacionId = 0; this.evaluaciones = [];
    if (!this.cursoId) return;
    this.api.getAsignacionesPorCurso(+this.cursoId).subscribe({
      next: data => {
        this.asignaciones = data.map((a: any) => ({
          ...a,
          label: this.asignaturasList.find((s: any) => s.id === a.asignaturaId)?.nombre || `Asig. ${a.id}`
        }));
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

  getAsignacion(id: number) {
    const a = this.asignaciones.find((a: any) => a.id === id);
    return a?.label || `Asig. ${id}`;
  }

  abrirModal() {
    this.errorMsg = '';
    this.nueva = { nombre: '', fecha: new Date().toISOString().split('T')[0], descripcion: '' };
    this.showModal = true;
  }

  crear() {
    if (!this.nueva.nombre || !this.asignacionId) return;
    this.errorMsg = '';
    this.api.crearEvaluacion({ ...this.nueva, asignacionDocenteId: +this.asignacionId }).subscribe({
      next: () => {
        this.showModal = false;
        this.nueva = { nombre: '', fecha: new Date().toISOString().split('T')[0], descripcion: '' };
        this.exitoMsg = 'Evaluación creada correctamente';
        this.onAsignacion();
      },
      error: (err) => {
        this.errorMsg = err?.status === 409 ? 'La evaluación ya existe' : 'Error al crear la evaluación';
      }
    });
  }
}
