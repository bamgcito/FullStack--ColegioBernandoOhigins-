import { Component, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { IonContent, IonIcon } from '@ionic/angular/standalone';
import { LayoutComponent } from '../../../shared/components/layout/layout.component';
import { addIcons } from 'ionicons';
import { schoolOutline } from 'ionicons/icons';
import { AuthService } from '../../../core/services/auth.service';
import * as Mock from '../../../shared/mocks/mock-data';

@Component({
  selector: 'app-profesor-mis-cursos',
  standalone: true,
  imports: [CommonModule, IonContent, IonIcon, LayoutComponent],
  templateUrl: './profesor-mis-cursos.page.html',
  styleUrls: ['./profesor-mis-cursos.page.scss'],
  host: { class: 'ion-page' }
})
export class ProfesorMisCursosPage implements OnInit {
  cursos: any[] = [];
  constructor(private auth: AuthService) { addIcons({ schoolOutline }); }
  ngOnInit() {
    const uid = this.auth.currentUser?.id ?? 0;
    this.cursos = Mock.ASIGNACIONES_MOCK.filter(a => a.profesorId === uid).map(a => {
      const c = Mock.CURSOS_MOCK.find(c => c.id === a.cursoId);
      const s = Mock.ASIGNATURAS_MOCK.find(s => s.id === a.asignaturaId);
      const alumnos = Mock.ALUMNOS_MOCK.filter(al => c?.alumnosIds.includes(al.id));
      return { asignatura: s?.nombre || '—', cursoNombre: c ? `${c.nivel}${c.letra}` : '—', alumnos };
    });
  }
}
