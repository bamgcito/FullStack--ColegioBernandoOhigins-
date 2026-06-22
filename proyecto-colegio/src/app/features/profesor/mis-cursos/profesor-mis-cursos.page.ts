import { Component, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { IonContent, IonIcon } from '@ionic/angular/standalone';
import { LayoutComponent } from '../../../shared/components/layout/layout.component';
import { addIcons } from 'ionicons';
import { schoolOutline } from 'ionicons/icons';
import { AuthService } from '../../../core/services/auth.service';
import { ApiService } from '../../../core/services/api.service';
import { forkJoin } from 'rxjs';

@Component({
  selector: 'app-profesor-mis-cursos',
  standalone: true,
  imports: [CommonModule, IonContent, IonIcon, LayoutComponent],
  templateUrl: './profesor-mis-cursos.page.html',
  styleUrls: ['./profesor-mis-cursos.page.scss']
})
export class ProfesorMisCursosPage implements OnInit {
  cursos: any[] = [];
  loading = true;
  uid = 0;

  constructor(private auth: AuthService, private api: ApiService) { addIcons({ schoolOutline }); }

  ngOnInit() {
    this.uid = this.auth.currentUser?.id ?? 0;
    const uid = this.uid;
    if (!uid) { this.loading = false; return; }
    this.api.getCursos().subscribe({
      next: cursos => {
        if (!cursos.length) { this.cursos = []; this.loading = false; return; }
        forkJoin(cursos.map((c: any) => this.api.getAsignacionesPorCurso(c.id))).subscribe({
          next: (res: any[]) => {
            const asigs = res.flat().filter((a: any) => a.profesorId === uid);
            const cursoIds = [...new Set<number>(asigs.map((a: any) => a.cursoId))];
            const cursosProfesor = cursos.filter((c: any) => cursoIds.includes(c.id));
            if (!cursosProfesor.length) { this.cursos = []; this.loading = false; return; }
            forkJoin(cursosProfesor.map((c: any) => this.api.getAlumnosDeCurso(c.id))).subscribe({
              next: alumnosArr => {
                this.cursos = cursosProfesor.map((c: any, i: number) => ({
                  ...c,
                  cursoNombre: `${c.nivel}${c.letra}`,
                  alumnos: alumnosArr[i] || []
                }));
                this.loading = false;
              },
              error: () => { this.loading = false; }
            });
          },
          error: () => { this.loading = false; }
        });
      },
      error: () => { this.loading = false; }
    });
  }
}
