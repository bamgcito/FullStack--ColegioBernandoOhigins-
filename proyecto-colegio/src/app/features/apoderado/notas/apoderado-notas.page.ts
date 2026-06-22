import { Component, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { IonContent, IonIcon } from '@ionic/angular/standalone';
import { LayoutComponent } from '../../../shared/components/layout/layout.component';
import { addIcons } from 'ionicons';
import { bookOutline, statsChartOutline, documentTextOutline, chevronDownOutline } from 'ionicons/icons';
import { AuthService } from '../../../core/services/auth.service';
import { ApiService } from '../../../core/services/api.service';
import { ApoderadoStateService } from '../../../core/services/apoderado-state.service';

@Component({
  selector: 'app-apoderado-notas',
  standalone: true,
  imports: [CommonModule, IonContent, IonIcon, LayoutComponent],
  templateUrl: './apoderado-notas.page.html',
  styleUrls: ['./apoderado-notas.page.scss']
})
export class ApoderadoNotasPage implements OnInit {
  pupiloNombre = '';
  promedio = 0;
  totalNotas = 0;
  asignaturas: { id: number; nombre: string; notas: any[]; promedio: number }[] = [];
  openIds = new Set<number>();
  sinAlumno = false;

  constructor(private auth: AuthService, private api: ApiService, private state: ApoderadoStateService) {
    addIcons({ bookOutline, statsChartOutline, documentTextOutline, chevronDownOutline });
  }

  ngOnInit() {
    const apoderadoId = this.auth.currentUser?.id ?? 0;
    if (!apoderadoId) { this.sinAlumno = true; return; }

    const cargarConPupilo = (pupilo: any) => {
      this.pupiloNombre = `${pupilo.nombre} ${pupilo.apellido}`;
      const usuarioId = pupilo.usuarioId;

      this.api.getNotasAlumno(usuarioId).subscribe({
        next: notas => {
          this.totalNotas = notas.length;

          const mapa: Record<number, { id: number; nombre: string; notas: any[]; promedio: number }> = {};
          notas.forEach((n: any) => {
            const aid = n.asignacionId || 0;
            if (!mapa[aid]) {
              mapa[aid] = { id: aid, nombre: n.nombreAsignatura || 'Sin asignatura', notas: [], promedio: 0 };
            }
            mapa[aid].notas.push(n);
          });

          this.asignaturas = Object.values(mapa).map(a => {
            const suma = a.notas.reduce((s, n) => s + (n.valor || 0), 0);
            return { ...a, promedio: Math.round((suma / a.notas.length) * 10) / 10 };
          });

          if (this.asignaturas.length > 0) {
            const sumaPromedios = this.asignaturas.reduce((s, a) => s + a.promedio, 0);
            this.promedio = Math.round((sumaPromedios / this.asignaturas.length) * 10) / 10;
            this.openIds.add(this.asignaturas[0].id);
          }
        }
      });
    };

    if (this.state.pupilo) { cargarConPupilo(this.state.pupilo); return; }

    this.api.getAlumnosDeApoderado(apoderadoId).subscribe({
      next: alumnos => {
        if (!alumnos?.length) { this.sinAlumno = true; return; }
        this.state.pupilo = alumnos[0];
        cargarConPupilo(alumnos[0]);
      }
    });
  }

  toggle(id: number) { this.openIds.has(id) ? this.openIds.delete(id) : this.openIds.add(id); }
  isOpen(id: number) { return this.openIds.has(id); }
  notaClass(n: number) { if (n >= 6) return 'excelente'; if (n >= 5) return 'buena'; if (n >= 4) return 'suficiente'; return 'reprobado'; }
  promedioClass(n: number) { if (n >= 6) return 'excelente'; if (n >= 5) return 'buena'; if (n >= 4) return 'suficiente'; return n === 0 ? '' : 'reprobado'; }
}
