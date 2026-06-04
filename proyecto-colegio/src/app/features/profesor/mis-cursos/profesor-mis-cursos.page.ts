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

  constructor(private auth: AuthService, private api: ApiService) { addIcons({ schoolOutline }); }

  ngOnInit() {
    const uid = this.auth.currentUser?.id ?? 0;
    if (!uid) { this.loading = false; return; }
    this.api.getCursosByProfesor(uid).subscribe({
      next: cursos => {
        if (!cursos.length) { this.cursos = []; this.loading = false; return; }
        forkJoin(cursos.map((c: any) => this.api.getAlumnosDeCurso(c.id))).subscribe({
          next: alumnosArr => {
            this.cursos = cursos.map((c: any, i: number) => ({
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
  }
}
