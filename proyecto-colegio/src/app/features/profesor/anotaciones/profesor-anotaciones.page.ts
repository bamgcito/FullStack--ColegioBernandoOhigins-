import { Component, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { IonContent, IonIcon } from '@ionic/angular/standalone';
import { LayoutComponent } from '../../../shared/components/layout/layout.component';
import { addIcons } from 'ionicons';
import { addOutline, closeOutline } from 'ionicons/icons';
import { AuthService } from '../../../core/services/auth.service';
import { ApiService } from '../../../core/services/api.service';
import { forkJoin } from 'rxjs';

@Component({
  selector: 'app-profesor-anotaciones',
  standalone: true,
  imports: [CommonModule, FormsModule, IonContent, IonIcon, LayoutComponent],
  templateUrl: './profesor-anotaciones.page.html',
  styleUrls: ['./profesor-anotaciones.page.scss'],
  host: { class: 'ion-page' }
})
export class ProfesorAnotacionesPage implements OnInit {
  asignaciones: any[] = [];
  anotaciones: any[] = [];
  alumnos: any[] = [];
  showModal = false;
  nueva = { rutAlumno: '', asignacionDocenteId: 0, tipo: 'POSITIVA', descripcion: '' };

  constructor(private auth: AuthService, private api: ApiService) { addIcons({ addOutline, closeOutline }); }
  ngOnInit() { this.cargar(); }

  cargar() {
    const uid = this.auth.currentUser?.id ?? 0;
    forkJoin({ cursos: this.api.getCursos(), asignaturas: this.api.getAsignaturas() }).subscribe({
      next: ({ cursos, asignaturas }) => {
        if (!cursos.length) return;
        forkJoin(cursos.map((c: any) => this.api.getAsignacionesPorCurso(c.id))).subscribe({
          next: (res: any[]) => {
            this.asignaciones = res.flat().filter((a: any) => a.profesorId === uid).map((a: any) => {
              const c = cursos.find((c: any) => c.id === a.cursoId);
              const s = asignaturas.find((s: any) => s.id === a.asignaturaId);
              return { ...a, label: `${s?.nombre || '—'} — ${c?.nivel || ''}${c?.letra || ''}` };
            });
            if (!this.asignaciones.length) return;
            // Cargar alumnos de los cursos de este profesor
            const cursoIds = [...new Set(this.asignaciones.map((a: any) => a.cursoId))];
            const cursosDelProfe = cursos.filter((c: any) => cursoIds.includes(c.id));
            const alumnoRuts: string[] = cursosDelProfe.flatMap((c: any) => c.alumnoRuts || c.alumnos || []);
            this.alumnos = alumnoRuts; // si el backend devuelve ruts en el curso, sino se deja vacío

            forkJoin(this.asignaciones.map((a: any) => this.api.getAnotacionesPorAsignacion(a.id))).subscribe({
              next: (aRes: any[]) => { this.anotaciones = aRes.flat(); }
            });
          }
        });
      }
    });
  }

  getAlumno(rut: string) {
    const a = this.alumnos.find((al: any) => al.rut === rut || al === rut);
    return a?.nombre ? `${a.nombre} ${a.apellido}` : rut;
  }

  badge(t: string) { return { POSITIVA: 'badge-success', NEGATIVA: 'badge-danger', NEUTRA: 'badge-neutral' }[t] || 'badge-neutral'; }

  crear() {
    if (!this.nueva.rutAlumno || !this.nueva.asignacionDocenteId || !this.nueva.descripcion) return;
    this.api.registrarAnotacion(this.nueva).subscribe({
      next: data => {
        this.anotaciones.unshift(data);
        this.showModal = false;
        this.nueva = { rutAlumno: '', asignacionDocenteId: 0, tipo: 'POSITIVA', descripcion: '' };
      }
    });
  }
}