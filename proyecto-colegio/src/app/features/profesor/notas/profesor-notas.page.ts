import { Component, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { IonContent, IonIcon } from '@ionic/angular/standalone';
import { LayoutComponent } from '../../../shared/components/layout/layout.component';
import { addIcons } from 'ionicons';
import { saveOutline } from 'ionicons/icons';
import { AuthService } from '../../../core/services/auth.service';
import { ApiService } from '../../../core/services/api.service';
import { forkJoin } from 'rxjs';

@Component({
  selector: 'app-profesor-notas',
  standalone: true,
  imports: [CommonModule, FormsModule, IonContent, IonIcon, LayoutComponent],
  templateUrl: './profesor-notas.page.html',
  styleUrls: ['./profesor-notas.page.scss'],
  host: { class: 'ion-page' }
})
export class ProfesorNotasPage implements OnInit {
  asignaciones: any[] = [];
  evaluaciones: any[] = [];
  asignacionId = 0;
  evaluacionId = 0;
  alumnos: any[] = [];
  notasReg: Record<number, number> = {};
  guardado = false;

  constructor(private auth: AuthService, private api: ApiService) { addIcons({ saveOutline }); }

  ngOnInit() {
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
          }
        });
      }
    });
  }

  onAsig() {
    this.evaluaciones = []; this.evaluacionId = 0; this.alumnos = [];
    this.api.getEvaluacionesPorAsignacion(this.asignacionId).subscribe({ next: data => { this.evaluaciones = data; } });
  }

  onEval() {
    this.api.getNotasPorEvaluacion(this.evaluacionId).subscribe({
      next: notas => {
        const map: Record<number, number> = {};
        notas.forEach((n: any) => { map[n.alumnoId] = n.nota || 0; });
        this.api.getUsuarios().subscribe({
          next: usuarios => {
            this.alumnos = usuarios.filter((u: any) => (u.nombreRol || u.rol) === 'ALUMNO');
            this.alumnos.forEach(al => { this.notasReg[al.id] = map[al.id] ?? 0; });
          }
        });
      }
    });
  }

  notaClass(n: number) { if (n >= 6) return 'excelente'; if (n >= 5) return 'buena'; if (n >= 4) return 'suficiente'; if (n > 0) return 'reprobado'; return ''; }

  guardar() {
    const reqs = this.alumnos.filter(al => this.notasReg[al.id] > 0)
      .map(al => this.api.crearNota({ alumnoId: al.id, evaluacionId: this.evaluacionId, nota: this.notasReg[al.id] }));
    if (!reqs.length) { this.guardado = true; return; }
    forkJoin(reqs).subscribe({ next: () => { this.guardado = true; } });
  }
}