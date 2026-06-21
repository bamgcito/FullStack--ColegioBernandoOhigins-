import { Component, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { IonContent, IonIcon } from '@ionic/angular/standalone';
import { LayoutComponent } from '../../../shared/components/layout/layout.component';
import { addIcons } from 'ionicons';
import { analyticsOutline, checkmarkOutline, closeOutline, timeOutline, bookOutline, chevronDownOutline } from 'ionicons/icons';
import { AuthService } from '../../../core/services/auth.service';
import { ApiService } from '../../../core/services/api.service';
import { forkJoin, of } from 'rxjs';
import { catchError } from 'rxjs/operators';

@Component({
  selector: 'app-alumno-asistencia',
  standalone: true,
  imports: [CommonModule, IonContent, IonIcon, LayoutComponent],
  templateUrl: './alumno-asistencia.page.html',
  styleUrls: ['./alumno-asistencia.page.scss']
})
export class AlumnoAsistenciaPage implements OnInit {
  porcentaje = 0;
  presentes = 0; ausentes = 0; atrasados = 0;
  asignaturas: any[] = [];
  openIds: Set<number> = new Set();

  constructor(private auth: AuthService, private api: ApiService) {
    addIcons({ analyticsOutline, checkmarkOutline, closeOutline, timeOutline, bookOutline, chevronDownOutline });
  }

  ngOnInit() {
    const uid = this.auth.currentUser?.id ?? 0;
    if (!uid) return;

    this.api.getCursoDeAlumno(uid).pipe(catchError(() => of(null))).subscribe(curso => {
      if (!curso) return;

      forkJoin({
        asignaciones: this.api.getAsignacionesPorCurso(curso.id).pipe(catchError(() => of([]))),
        asistencia: this.api.getAsistenciaAlumno(uid).pipe(catchError(() => of([])))
      }).subscribe({ next: ({ asignaciones, asistencia }) => {
        this.presentes = asistencia.filter((r: any) => r.estado === 'PRESENTE').length;
        this.ausentes = asistencia.filter((r: any) => r.estado === 'AUSENTE').length;
        this.atrasados = asistencia.filter((r: any) => r.estado === 'ATRASADO').length;
        this.porcentaje = asistencia.length
          ? Math.round((this.presentes + this.atrasados) / asistencia.length * 100)
          : 0;

        const mapa: Record<number, any> = {};
        asignaciones.forEach((a: any) => {
          mapa[a.id] = { id: a.id, nombre: a.nombreAsignatura || `Asignatura ${a.asignaturaId}`, registros: [] };
        });
        asistencia.forEach((r: any) => {
          const aid = r.asignacionId || r.asignacionDocenteId || 0;
          if (mapa[aid]) mapa[aid].registros.push(r);
        });
        this.asignaturas = Object.values(mapa);
        if (this.asignaturas.length > 0) this.openIds.add(this.asignaturas[0].id);
      }});
    });
  }

  toggle(id: number) { this.openIds.has(id) ? this.openIds.delete(id) : this.openIds.add(id); }
  isOpen(id: number) { return this.openIds.has(id); }
  badge(e: string) { return { PRESENTE: 'badge-success', AUSENTE: 'badge-danger', ATRASADO: 'badge-warning' }[e] || 'badge-neutral'; }
  pct(registros: any[]) {
    if (!registros.length) return 0;
    return Math.round(registros.filter((r: any) => r.estado !== 'AUSENTE').length / registros.length * 100);
  }
}
