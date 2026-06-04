import { Component, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { IonContent, IonIcon } from '@ionic/angular/standalone';
import { LayoutComponent } from '../../../shared/components/layout/layout.component';
import { addIcons } from 'ionicons';
import { starOutline } from 'ionicons/icons';
import { AuthService } from '../../../core/services/auth.service';
import { ApiService } from '../../../core/services/api.service';
import { forkJoin } from 'rxjs';

@Component({
  selector: 'app-apoderado-asistencia',
  standalone: true,
  imports: [CommonModule, IonContent, IonIcon, LayoutComponent],
  templateUrl: './apoderado-asistencia.page.html',
  styleUrls: ['./apoderado-asistencia.page.scss']
})
export class ApoderadoAsistenciaPage implements OnInit {
  pupiloNombre = '';
  registros: any[] = [];
  porcentaje = 0;
  sinAlumno = false;

  constructor(private auth: AuthService, private api: ApiService) { addIcons({ starOutline }); }

  ngOnInit() {
    const apoderadoId = this.auth.currentUser?.id ?? 0;
    if (!apoderadoId) { this.sinAlumno = true; return; }
    this.api.getAlumnosDeApoderado(apoderadoId).subscribe({
      next: alumnos => {
        if (!alumnos?.length) { this.sinAlumno = true; return; }
        const pupilo = alumnos[0];
        this.pupiloNombre = `${pupilo.nombre} ${pupilo.apellido}`;
        const rut = pupilo.rut;
        forkJoin({
          registros: this.api.getAsistenciaAlumno(rut),
          porcentaje: this.api.getPorcentajeAsistencia(rut)
        }).subscribe({
          next: ({ registros, porcentaje }) => {
            this.registros = registros;
            this.porcentaje = typeof porcentaje === 'number' ? porcentaje : (porcentaje?.porcentajeAsistencia ?? 0);
          }
        });
      }
    });
  }

  badge(e: string) { return { PRESENTE: 'badge-success', AUSENTE: 'badge-danger', ATRASADO: 'badge-warning' }[e] || 'badge-neutral'; }
}
