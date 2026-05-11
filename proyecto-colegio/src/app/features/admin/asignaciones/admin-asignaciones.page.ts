import { Component, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { IonContent, IonIcon } from '@ionic/angular/standalone';
import { LayoutComponent } from '../../../shared/components/layout/layout.component';
import { addIcons } from 'ionicons';
import { addOutline, closeOutline } from 'ionicons/icons';
import { ApiService } from '../../../core/services/api.service';
import { forkJoin } from 'rxjs';

@Component({
  selector: 'app-admin-asignaciones',
  standalone: true,
  imports: [CommonModule, FormsModule, IonContent, IonIcon, LayoutComponent],
  templateUrl: './admin-asignaciones.page.html',
  styleUrls: ['./admin-asignaciones.page.scss'],
  host: { class: 'ion-page' }
})
export class AdminAsignacionesPage implements OnInit {
  asignaciones: any[] = [];
  profesores: any[] = [];
  cursos: any[] = [];
  asignaturasList: any[] = [];
  showModal = false;
  nuevo = { profesorId: 0, cursoId: 0, asignaturaId: 0 };

  constructor(private api: ApiService) { addIcons({ addOutline, closeOutline }); }
  ngOnInit() { this.cargar(); }

  cargar() {
    forkJoin({ cursos: this.api.getCursos(), usuarios: this.api.getUsuarios(), asignaturas: this.api.getAsignaturas() }).subscribe({
      next: ({ cursos, usuarios, asignaturas }) => {
        this.cursos = cursos;
        this.profesores = usuarios.filter((u: any) => (u.nombreRol || u.rol) === 'PROFESOR');
        this.asignaturasList = asignaturas;
        if (!cursos.length) return;
        forkJoin(cursos.map((c: any) => this.api.getAsignacionesPorCurso(c.id))).subscribe({
          next: (res: any[]) => { this.asignaciones = res.flat(); }
        });
      }
    });
  }

  getProfesor(id: number) { const p = this.profesores.find(p => p.id === id); return p ? `${p.nombre} ${p.apellido}` : '—'; }
  getCurso(id: number) { const c = this.cursos.find(c => c.id === id); return c ? `${c.nivel}${c.letra}` : '—'; }
  getAsignatura(id: number) { return this.asignaturasList.find(a => a.id === id)?.nombre || '—'; }

  crear() {
    if (!this.nuevo.profesorId || !this.nuevo.cursoId || !this.nuevo.asignaturaId) return;
    this.api.crearAsignacion(this.nuevo).subscribe({
      next: () => { this.showModal = false; this.nuevo = { profesorId: 0, cursoId: 0, asignaturaId: 0 }; this.cargar(); }
    });
  }
}