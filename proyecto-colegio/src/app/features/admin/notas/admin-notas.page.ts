import { Component, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { IonContent, IonIcon } from '@ionic/angular/standalone';
import { LayoutComponent } from '../../../shared/components/layout/layout.component';
import { addIcons } from 'ionicons';
import { starOutline } from 'ionicons/icons';
import { NOTAS_MOCK, ALUMNOS_MOCK, EVALUACIONES_MOCK, Nota } from '../../../shared/mocks/mock-data';

@Component({
  selector: 'app-admin-notas',
  standalone: true,
  imports: [CommonModule, IonContent, IonIcon, LayoutComponent],
  templateUrl: './admin-notas.page.html',
  styleUrls: ['./admin-notas.page.scss'],
  host: { class: 'ion-page' }
})
export class AdminNotasPage implements OnInit {
  notas: Nota[] = [];
  constructor() { addIcons({ starOutline }); }
  ngOnInit() { this.notas = [...NOTAS_MOCK]; }
  getAlumno(id: number) { const a = ALUMNOS_MOCK.find(a => a.id === id); return a ? `${a.nombre} ${a.apellido}` : '—'; }
  getEvaluacion(id: number) { return EVALUACIONES_MOCK.find(e => e.id === id)?.nombre || '—'; }
  notaClass(n: number) { if (n >= 6) return 'excelente'; if (n >= 5) return 'buena'; if (n >= 4) return 'suficiente'; return 'reprobado'; }
}
