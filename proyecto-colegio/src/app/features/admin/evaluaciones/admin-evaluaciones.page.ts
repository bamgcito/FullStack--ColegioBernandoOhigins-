import { Component, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { IonContent, IonIcon } from '@ionic/angular/standalone';
import { LayoutComponent } from '../../../shared/components/layout/layout.component';
import { addIcons } from 'ionicons';
import { documentTextOutline } from 'ionicons/icons';
import { EVALUACIONES_MOCK, ASIGNACIONES_MOCK, PROFESORES_MOCK, CURSOS_MOCK, ASIGNATURAS_MOCK, Evaluacion } from '../../../shared/mocks/mock-data';

@Component({
  selector: 'app-admin-evaluaciones',
  standalone: true,
  imports: [CommonModule, IonContent, IonIcon, LayoutComponent],
  templateUrl: './admin-evaluaciones.page.html',
  styleUrls: ['./admin-evaluaciones.page.scss'],
  host: { class: 'ion-page' }
})
export class AdminEvaluacionesPage implements OnInit {
  evaluaciones: Evaluacion[] = [];
  constructor() { addIcons({ documentTextOutline }); }
  ngOnInit() { this.evaluaciones = [...EVALUACIONES_MOCK]; }
  getAsignacion(id: number) {
    const a = ASIGNACIONES_MOCK.find(a => a.id === id);
    const p = PROFESORES_MOCK.find(p => p.id === a?.profesorId);
    const c = CURSOS_MOCK.find(c => c.id === a?.cursoId);
    const s = ASIGNATURAS_MOCK.find(s => s.id === a?.asignaturaId);
    return `${s?.nombre || '—'} — ${c?.nivel || ''}${c?.letra || ''} (${p?.apellido || '—'})`;
  }
}
