import { Component, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { IonContent, IonIcon } from '@ionic/angular/standalone';
import { LayoutComponent } from '../../../shared/components/layout/layout.component';
import { addIcons } from 'ionicons';
import { pencilOutline } from 'ionicons/icons';
import { ANOTACIONES_MOCK, ALUMNOS_MOCK, Anotacion } from '../../../shared/mocks/mock-data';

@Component({
  selector: 'app-admin-anotaciones',
  standalone: true,
  imports: [CommonModule, IonContent, IonIcon, LayoutComponent],
  templateUrl: './admin-anotaciones.page.html',
  styleUrls: ['./admin-anotaciones.page.scss'],
  host: { class: 'ion-page' }
})
export class AdminAnotacionesPage implements OnInit {
  anotaciones: Anotacion[] = [];
  constructor() { addIcons({ pencilOutline }); }
  ngOnInit() { this.anotaciones = [...ANOTACIONES_MOCK]; }
  getAlumno(id: number) { const a = ALUMNOS_MOCK.find(a => a.id === id); return a ? `${a.nombre} ${a.apellido}` : '—'; }
  badge(t: string) { return { POSITIVA: 'badge-success', NEGATIVA: 'badge-danger', NEUTRA: 'badge-neutral' }[t] || 'badge-neutral'; }
}
