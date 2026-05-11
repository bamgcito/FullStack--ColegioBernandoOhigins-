import { Component, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { IonContent, IonIcon } from '@ionic/angular/standalone';
import { LayoutComponent } from '../../../shared/components/layout/layout.component';
import { addIcons } from 'ionicons';
import { bookOutline, statsChartOutline, documentTextOutline, chevronDownOutline } from 'ionicons/icons';
import { AuthService } from '../../../core/services/auth.service';
import { ApiService } from '../../../core/services/api.service';

@Component({
  selector: 'app-alumno-notas',
  standalone: true,
  imports: [CommonModule, IonContent, IonIcon, LayoutComponent],
  templateUrl: './alumno-notas.page.html',
  styleUrls: ['./alumno-notas.page.scss'],
  host: { class: 'ion-page' }
})
export class AlumnoNotasPage implements OnInit {
  promedio = 0;
  asignaturas: any[] = [];
  openIds: Set<number> = new Set();

  constructor(private auth: AuthService, private api: ApiService) {
    addIcons({ bookOutline, statsChartOutline, documentTextOutline, chevronDownOutline });
  }

  ngOnInit() {
    const rut = this.auth.currentUser?.rut ?? '';
    if (!rut) return;

    this.api.getNotasAlumno(rut).subscribe({
      next: notas => {
        this.api.getPromedioAlumno(rut).subscribe({
          next: p => { this.promedio = typeof p === 'number' ? p : p?.promedio ?? 0; }
        });
        const mapa: Record<number, any> = {};
        notas.forEach((n: any) => {
          const aid = n.asignacionId || n.asignacionDocenteId || 0;
          if (!mapa[aid]) mapa[aid] = { id: aid, nombre: n.asignatura || `Asig. ${aid}`, notas: [] };
          mapa[aid].notas.push(n);
        });
        this.asignaturas = Object.values(mapa).map((a: any) => ({
          ...a,
          promedio: Math.round((a.notas.reduce((s: number, n: any) => s + (n.nota || 0), 0) / a.notas.length) * 10) / 10
        }));
        if (this.asignaturas.length > 0) this.openIds.add(this.asignaturas[0].id);
      }
    });
  }

  toggle(id: number) { this.openIds.has(id) ? this.openIds.delete(id) : this.openIds.add(id); }
  isOpen(id: number) { return this.openIds.has(id); }
  notaClass(n: number) { if (n >= 6) return 'excelente'; if (n >= 5) return 'buena'; if (n >= 4) return 'suficiente'; return 'reprobado'; }
}