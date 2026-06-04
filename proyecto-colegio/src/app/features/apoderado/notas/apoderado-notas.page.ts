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
  selector: 'app-apoderado-notas',
  standalone: true,
  imports: [CommonModule, IonContent, IonIcon, LayoutComponent],
  templateUrl: './apoderado-notas.page.html',
  styleUrls: ['./apoderado-notas.page.scss']
})
export class ApoderadoNotasPage implements OnInit {
  pupiloNombre = '';
  notas: any[] = [];
  promedio = 0;
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
          notas: this.api.getNotasAlumno(rut),
          promedio: this.api.getPromedioAlumno(rut)
        }).subscribe({
          next: ({ notas, promedio }) => {
            this.notas = notas;
            this.promedio = typeof promedio === 'number' ? promedio : (promedio?.promedioGeneral ?? 0);
          }
        });
      }
    });
  }

  notaClass(n: number) {
    if (n >= 6) return 'excelente';
    if (n >= 5) return 'buena';
    if (n >= 4) return 'suficiente';
    return 'reprobado';
  }
}
