import { Component, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { IonContent, IonIcon } from '@ionic/angular/standalone';
import { LayoutComponent } from '../../../shared/components/layout/layout.component';
import { addIcons } from 'ionicons';
import { starOutline } from 'ionicons/icons';
import { AuthService } from '../../../core/services/auth.service';
import * as Mock from '../../../shared/mocks/mock-data';

@Component({
  selector: 'app-apoderado-asistencia',
  standalone: true,
  imports: [CommonModule, IonContent, IonIcon, LayoutComponent],
  templateUrl: './apoderado-asistencia.page.html',
  styleUrls: ['./apoderado-asistencia.page.scss'],
  host: { class: 'ion-page' }
})
export class ApoderadoAsistenciaPage implements OnInit {
  alumnoId = 0;
  pupiloNombre = '';
  items: any[] = [];
  constructor(private auth: AuthService) { addIcons({ starOutline }); }
  ngOnInit() {
    const uid = this.auth.currentUser?.id ?? 0;
    const ap = Mock.APODERADOS_MOCK.find(a => a.id === uid);
    this.alumnoId = ap?.alumnosIds[0] ?? 0;
    const al = Mock.ALUMNOS_MOCK.find(a => a.id === this.alumnoId);
    this.pupiloNombre = al ? `${al.nombre} ${al.apellido}` : '';
    this.items = Mock.getAsistenciaByAlumno(this.alumnoId);
  }
  badge(v: string) { return { POSITIVA: 'badge-success', NEGATIVA: 'badge-danger', NEUTRA: 'badge-neutral', PRESENTE: 'badge-success', AUSENTE: 'badge-danger', ATRASADO: 'badge-warning' }[v] || 'badge-neutral'; }
  notaClass(n: number) { if (n >= 6) return 'excelente'; if (n >= 5) return 'buena'; if (n >= 4) return 'suficiente'; return 'reprobado'; }
}
