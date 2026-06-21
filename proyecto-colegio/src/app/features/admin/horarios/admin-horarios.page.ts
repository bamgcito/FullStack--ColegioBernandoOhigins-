import { Component, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { IonContent, IonIcon } from '@ionic/angular/standalone';
import { LayoutComponent } from '../../../shared/components/layout/layout.component';
import { addIcons } from 'ionicons';
import { calendarOutline, addOutline, closeOutline, trashOutline } from 'ionicons/icons';
import { ApiService } from '../../../core/services/api.service';
import { forkJoin } from 'rxjs';

@Component({
  selector: 'app-admin-horarios',
  standalone: true,
  imports: [CommonModule, FormsModule, IonContent, IonIcon, LayoutComponent],
  templateUrl: './admin-horarios.page.html',
  styleUrls: ['./admin-horarios.page.scss']
})
export class AdminHorariosPage implements OnInit {
  cursos: any[] = [];
  cursoId = 0;
  asignaciones: any[] = [];
  asignaturasList: any[] = [];
  horarios: any[] = [];
  showModal = false;
  errorMsg = '';
  exitoMsg = '';
  nuevo = { asignacionDocenteId: 0, diaSemana: '', bloque: 1, horaInicio: '', horaFin: '' };
  dias = ['LUNES', 'MARTES', 'MIERCOLES', 'JUEVES', 'VIERNES'];

  constructor(private api: ApiService) {
    addIcons({ calendarOutline, addOutline, closeOutline, trashOutline });
  }

  ngOnInit() {
    forkJoin({ cursos: this.api.getCursos(), asignaturas: this.api.getAsignaturas() }).subscribe({
      next: ({ cursos, asignaturas }) => { this.cursos = cursos; this.asignaturasList = asignaturas; }
    });
  }

  onCurso() {
    this.asignaciones = []; this.horarios = []; this.errorMsg = ''; this.exitoMsg = '';
    if (!this.cursoId) return;
    this.api.getAsignacionesPorCurso(+this.cursoId).subscribe({
      next: data => {
        this.asignaciones = data.map((a: any) => ({
          ...a,
          label: this.asignaturasList.find((s: any) => s.id === a.asignaturaId)?.nombre || `Asig. ${a.id}`
        }));
        this.cargarHorarios();
      }
    });
  }

  cargarHorarios() {
    if (!this.cursoId) return;
    this.api.getHorariosPorCurso(+this.cursoId).subscribe({
      next: data => { this.horarios = this.ordenar(data); }
    });
  }

  ordenar(data: any[]): any[] {
    const ord = ['LUNES', 'MARTES', 'MIERCOLES', 'JUEVES', 'VIERNES'];
    return [...data].sort((a, b) => {
      const da = ord.indexOf(a.diaSemana), db = ord.indexOf(b.diaSemana);
      return da !== db ? da - db : a.bloque - b.bloque;
    });
  }

  abrirModal() {
    this.errorMsg = '';
    this.nuevo = { asignacionDocenteId: 0, diaSemana: '', bloque: 1, horaInicio: '', horaFin: '' };
    this.showModal = true;
  }

  crear() {
    if (!this.nuevo.asignacionDocenteId || !this.nuevo.diaSemana || !this.nuevo.horaInicio || !this.nuevo.horaFin) return;
    this.errorMsg = '';
    this.api.crearHorario(this.nuevo).subscribe({
      next: () => {
        this.showModal = false;
        this.exitoMsg = 'Horario creado correctamente';
        this.cargarHorarios();
      },
      error: (err: any) => {
        const msg = err?.error?.error || '';
        this.errorMsg = msg || (err?.status === 409 ? 'Choque de horario detectado' : 'Error al crear el horario');
      }
    });
  }

  eliminar(id: number) {
    this.exitoMsg = '';
    this.api.eliminarHorario(id).subscribe({
      next: () => {
        this.exitoMsg = 'Horario eliminado';
        this.cargarHorarios();
      }
    });
  }
}
