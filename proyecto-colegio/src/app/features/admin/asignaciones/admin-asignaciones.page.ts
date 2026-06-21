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
  styleUrls: ['./admin-asignaciones.page.scss']
})
export class AdminAsignacionesPage implements OnInit {
  asignaciones: any[] = [];
  profesores: any[] = [];
  profesorMap: Record<number, string> = {};
  cursos: any[] = [];
  asignaturasList: any[] = [];
  showModal = false;
  errorMsg = '';
  exitoMsg = '';
  nuevo = { rutProfesor: '', cursoId: 0, asignaturaId: 0 };

  constructor(private api: ApiService) { addIcons({ addOutline, closeOutline }); }
  ngOnInit() { this.cargar(); }

  cargar() {
    forkJoin({ cursos: this.api.getCursos(), usuarios: this.api.getUsuarios(), asignaturas: this.api.getAsignaturas() }).subscribe({
      next: ({ cursos, usuarios, asignaturas }) => {
        this.cursos = cursos;
        this.asignaturasList = asignaturas;
        const profUsers = usuarios.filter((u: any) => (u.nombreRol || u.rol) === 'PROFESOR');
        if (profUsers.length) {
          forkJoin(profUsers.map((u: any) => this.api.getPerfilProfesor(u.id))).subscribe({
            next: perfiles => {
              this.profesores = perfiles.filter(Boolean);
              perfiles.forEach((p: any, i: number) => {
                if (p) this.profesorMap[profUsers[i].id] = `${p.nombre} ${p.apellido}`;
              });
            }
          });
        }
        if (!cursos.length) return;
        forkJoin(cursos.map((c: any) => this.api.getAsignacionesPorCurso(c.id))).subscribe({
          next: (res: any[]) => { this.asignaciones = res.flat(); }
        });
      }
    });
  }

  getProfesor(id: number) { return this.profesorMap[id] || '—'; }
  getCurso(id: number) { const c = this.cursos.find(c => c.id === id); return c ? `${c.nivel}${c.letra}` : '—'; }
  getAsignatura(id: number) { return this.asignaturasList.find(a => a.id === id)?.nombre || '—'; }

  abrirModal() {
    this.errorMsg = '';
    this.exitoMsg = '';
    this.nuevo = { rutProfesor: '', cursoId: 0, asignaturaId: 0 };
    this.showModal = true;
  }

  crear() {
    if (!this.nuevo.rutProfesor || !this.nuevo.cursoId || !this.nuevo.asignaturaId) return;
    this.errorMsg = '';
    this.api.crearAsignacion(this.nuevo).subscribe({
      next: (resp: any) => {
        const msg: string = resp?.mensaje || '';
        if (msg.toLowerCase().includes('exitosamente')) {
          this.showModal = false;
          this.nuevo = { rutProfesor: '', cursoId: 0, asignaturaId: 0 };
          this.exitoMsg = 'Asignación creada correctamente';
          this.cargar();
        } else {
          this.errorMsg = msg || 'Error al crear la asignación';
        }
      },
      error: () => {
        this.errorMsg = 'Error al crear la asignación. Intenta nuevamente.';
      }
    });
  }
}
