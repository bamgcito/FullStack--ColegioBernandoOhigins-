import { Component, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { IonContent, IonIcon } from '@ionic/angular/standalone';
import { LayoutComponent } from '../../../shared/components/layout/layout.component';
import { addIcons } from 'ionicons';
import { starOutline } from 'ionicons/icons';
import { AuthService } from '../../../core/services/auth.service';
import { ApiService } from '../../../core/services/api.service';
import { ApoderadoStateService } from '../../../core/services/apoderado-state.service';

@Component({
  selector: 'app-apoderado-anotaciones',
  standalone: true,
  imports: [CommonModule, IonContent, IonIcon, LayoutComponent],
  templateUrl: './apoderado-anotaciones.page.html',
  styleUrls: ['./apoderado-anotaciones.page.scss']
})
export class ApoderadoAnotacionesPage implements OnInit {
  pupiloNombre = '';
  anotaciones: any[] = [];
  sinAlumno = false;

  constructor(private auth: AuthService, private api: ApiService, private state: ApoderadoStateService) { addIcons({ starOutline }); }

  ngOnInit() {
    const apoderadoId = this.auth.currentUser?.id ?? 0;
    if (!apoderadoId) { this.sinAlumno = true; return; }

    const cargarConPupilo = (pupilo: any) => {
      this.pupiloNombre = `${pupilo.nombre} ${pupilo.apellido}`;
      this.api.getAnotacionesAlumno(pupilo.usuarioId).subscribe({
        next: data => { this.anotaciones = data; }
      });
    };

    if (this.state.pupilo) {
      cargarConPupilo(this.state.pupilo);
      return;
    }

    this.api.getAlumnosDeApoderado(apoderadoId).subscribe({
      next: alumnos => {
        if (!alumnos?.length) { this.sinAlumno = true; return; }
        const pupilo = alumnos[0];
        this.state.pupilo = pupilo;
        cargarConPupilo(pupilo);
      }
    });
  }

  badge(t: string) { return { POSITIVA: 'badge-success', NEGATIVA: 'badge-danger', NEUTRA: 'badge-neutral' }[t] || 'badge-neutral'; }
}
