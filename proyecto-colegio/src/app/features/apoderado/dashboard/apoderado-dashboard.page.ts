import { Component, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { RouterLink } from '@angular/router';
import { IonContent, IonIcon } from '@ionic/angular/standalone';
import { LayoutComponent } from '../../../shared/components/layout/layout.component';
import { addIcons } from 'ionicons';
import { statsChartOutline, checkmarkCircleOutline, pencilOutline } from 'ionicons/icons';
import { AuthService } from '../../../core/services/auth.service';
import { ApiService } from '../../../core/services/api.service';
import { forkJoin } from 'rxjs';

@Component({
  selector: 'app-apoderado-dashboard',
  standalone: true,
  imports: [CommonModule, RouterLink, IonContent, IonIcon, LayoutComponent],
  templateUrl: './apoderado-dashboard.page.html',
  styleUrls: ['./apoderado-dashboard.page.scss'],
  host: { class: 'ion-page' }
})
export class ApoderadoDashboardPage implements OnInit {
  pupilo: any = null;
  cursoLabel = '—'; profeJefeNombre = 'Sin asignar';
  promedio = 0; asistencia = 0;
  anotaciones: any[] = []; notas: any[] = [];

  constructor(private auth: AuthService, private api: ApiService) {
    addIcons({ statsChartOutline, checkmarkCircleOutline, pencilOutline });
  }

  ngOnInit() {
    // getUsuarios() solo devuelve id, rut, nombreRol — sin nombre/apellido
    // necesitamos buscar el alumno completo con getAlumnoPorId
    this.api.getUsuarios().subscribe({
      next: usuarios => {
        const usuarioAlumno = usuarios.find((u: any) => (u.nombreRol || u.rol) === 'ALUMNO');
        if (!usuarioAlumno) return;

        // Traer datos completos del alumno (nombre, apellido, rut)
        this.api.getAlumnoPorId(usuarioAlumno.id).subscribe({
          next: alumno => {
            this.pupilo = alumno;
            const rut = alumno.rut;

            forkJoin({
              notas: this.api.getNotasAlumno(rut),
              asistencia: this.api.getAsistenciaAlumno(rut),
              anotaciones: this.api.getAnotacionesAlumno(rut),
              promedio: this.api.getPromedioAlumno(rut),
              info: this.api.getInfoAlumno(rut)
            }).subscribe({
              next: ({ notas, asistencia, anotaciones, promedio, info }) => {
                this.notas = notas.slice(0, 4);
                this.promedio = typeof promedio === 'number' ? promedio : promedio?.promedio ?? 0;
                this.asistencia = asistencia.length ? Math.round(asistencia.filter((r: any) => r.estado !== 'AUSENTE').length / asistencia.length * 100) : 0;
                this.anotaciones = anotaciones.slice(0, 3);
                if (info?.curso) this.cursoLabel = `${info.curso.nivel || ''}${info.curso.letra || ''}`;
                if (info?.profesorJefe) this.profeJefeNombre = `${info.profesorJefe.nombre} ${info.profesorJefe.apellido}`;
              }
            });
          }
        });
      }
    });
  }

  notaClass(n: number) { if (n >= 6) return 'excelente'; if (n >= 5) return 'buena'; if (n >= 4) return 'suficiente'; return 'reprobado'; }
  badge(t: string) { return { POSITIVA: 'badge-success', NEGATIVA: 'badge-danger', NEUTRA: 'badge-neutral' }[t] || 'badge-neutral'; }
}