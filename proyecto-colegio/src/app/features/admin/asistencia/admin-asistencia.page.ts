import { Component, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { IonContent, IonIcon } from '@ionic/angular/standalone';
import { LayoutComponent } from '../../../shared/components/layout/layout.component';
import { addIcons } from 'ionicons';
import { checkmarkCircleOutline } from 'ionicons/icons';
import { ASISTENCIA_MOCK, ALUMNOS_MOCK, ASIGNACIONES_MOCK, ASIGNATURAS_MOCK, Asistencia } from '../../../shared/mocks/mock-data';

@Component({
  selector: 'app-admin-asistencia',
  standalone: true,
  imports: [CommonModule, IonContent, IonIcon, LayoutComponent],
  templateUrl: './admin-asistencia.page.html',
  styleUrls: ['./admin-asistencia.page.scss'],
  host: { class: 'ion-page' }
})
export class AdminAsistenciaPage implements OnInit {
  registros: Asistencia[] = [];
  constructor() { addIcons({ checkmarkCircleOutline }); }
  ngOnInit() { this.registros = [...ASISTENCIA_MOCK]; }
  getAlumno(id: number) { const a = ALUMNOS_MOCK.find(a => a.id === id); return a ? `${a.nombre} ${a.apellido}` : '—'; }
  getAsignatura(id: number) { const a = ASIGNACIONES_MOCK.find(a => a.id === id); return ASIGNATURAS_MOCK.find(s => s.id === a?.asignaturaId)?.nombre || '—'; }
  badge(e: string) { return { PRESENTE: 'badge-success', AUSENTE: 'badge-danger', ATRASADO: 'badge-warning' }[e] || 'badge-neutral'; }
}
