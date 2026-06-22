import { Component, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { IonContent, IonIcon } from '@ionic/angular/standalone';
import { LayoutComponent } from '../../../shared/components/layout/layout.component';
import { addIcons } from 'ionicons';
import { calendarOutline } from 'ionicons/icons';
import { ApiService } from '../../../core/services/api.service';
import { AuthService } from '../../../core/services/auth.service';
import { ApoderadoStateService } from '../../../core/services/apoderado-state.service';

@Component({
  selector: 'app-apoderado-horarios',
  standalone: true,
  imports: [CommonModule, IonContent, IonIcon, LayoutComponent],
  templateUrl: './apoderado-horarios.page.html',
  styleUrls: ['./apoderado-horarios.page.scss']
})
export class ApoderadoHorariosPage implements OnInit {
  horarios: any[] = [];
  loading = true;
  sinAlumno = false;
  pupiloNombre = '';
  dias = ['LUNES', 'MARTES', 'MIERCOLES', 'JUEVES', 'VIERNES'];

  constructor(
    private auth: AuthService,
    private api: ApiService,
    private state: ApoderadoStateService
  ) {
    addIcons({ calendarOutline });
  }

  ngOnInit() {
    const apoderadoId = this.auth.currentUser?.id ?? 0;
    if (!apoderadoId) { this.sinAlumno = true; this.loading = false; return; }

    const cargarConPupilo = (pupilo: any) => {
      this.pupiloNombre = `${pupilo.nombre} ${pupilo.apellido}`;
      const usuarioId = pupilo.usuarioId;
      this.api.getCursoDeAlumno(usuarioId).subscribe({
        next: (curso: any) => {
          if (!curso?.id) { this.loading = false; return; }
          this.api.getHorariosPorCurso(curso.id).subscribe({
            next: data => { this.horarios = this.ordenar(data); this.loading = false; },
            error: () => { this.loading = false; }
          });
        },
        error: () => { this.loading = false; }
      });
    };

    if (this.state.pupilo) { cargarConPupilo(this.state.pupilo); return; }

    this.api.getAlumnosDeApoderado(apoderadoId).subscribe({
      next: alumnos => {
        if (!alumnos?.length) { this.sinAlumno = true; this.loading = false; return; }
        this.state.pupilo = alumnos[0];
        cargarConPupilo(alumnos[0]);
      },
      error: () => { this.sinAlumno = true; this.loading = false; }
    });
  }

  ordenar(data: any[]): any[] {
    const ord = ['LUNES', 'MARTES', 'MIERCOLES', 'JUEVES', 'VIERNES'];
    return [...data].sort((a, b) => {
      const da = ord.indexOf(a.diaSemana), db = ord.indexOf(b.diaSemana);
      return da !== db ? da - db : a.bloque - b.bloque;
    });
  }

  porDia(dia: string) { return this.horarios.filter(h => h.diaSemana === dia); }
}
