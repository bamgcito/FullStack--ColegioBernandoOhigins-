import { Component, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { IonContent, IonIcon } from '@ionic/angular/standalone';
import { LayoutComponent } from '../../../shared/components/layout/layout.component';
import { addIcons } from 'ionicons';
import { addOutline, closeOutline, personOutline, schoolOutline } from 'ionicons/icons';
import { ApiService } from '../../../core/services/api.service';
import { forkJoin } from 'rxjs';

@Component({
  selector: 'app-admin-cursos',
  standalone: true,
  imports: [CommonModule, FormsModule, IonContent, IonIcon, LayoutComponent],
  templateUrl: './admin-cursos.page.html',
  styleUrls: ['./admin-cursos.page.scss']
})
export class AdminCursosPage implements OnInit {
  cursos: any[] = [];
  profesores: any[] = [];
  loading = false;

  showModal = false;
  errorMsg = '';
  exitoMsg = '';
  nuevo = { nivel: '', letra: '', anio: 2025, profesorJefeId: null as number | null };

  showModalProfesor = false;
  errorMsgProfesor = '';
  cursoSel: any = null;
  profesorJefeRutSel = '';

  showModalAlumno = false;
  errorMsgAlumno = '';
  rutAlumnoInput = '';

  showModalListaAlumnos = false;
  listaAlumnosSel: any[] = [];

  constructor(private api: ApiService) {
    addIcons({ addOutline, closeOutline, personOutline, schoolOutline });
  }

  ngOnInit() { this.cargar(); }

  cargar() {
    this.loading = true;
    forkJoin({ cursos: this.api.getCursos(), usuarios: this.api.getUsuarios() }).subscribe({
      next: ({ cursos, usuarios }) => {
        this.cursos = cursos;
        this.loading = false;
        const profUsers = usuarios.filter((u: any) => (u.nombreRol || u.rol) === 'PROFESOR');
        if (!profUsers.length) return;
        forkJoin(profUsers.map((u: any) => this.api.getPerfilProfesor(u.id))).subscribe({
          next: perfiles => { this.profesores = perfiles.filter(Boolean); }
        });
      },
      error: () => { this.loading = false; }
    });
  }

  // --- Modal: Crear curso ---
  abrirModal() {
    this.errorMsg = '';
    this.exitoMsg = '';
    this.nuevo = { nivel: '', letra: '', anio: 2025, profesorJefeId: null };
    this.showModal = true;
  }

  crear() {
    if (!this.nuevo.nivel || !this.nuevo.letra) return;
    this.errorMsg = '';
    this.api.crearCurso(this.nuevo).subscribe({
      next: () => {
        this.showModal = false;
        this.nuevo = { nivel: '', letra: '', anio: 2025, profesorJefeId: null };
        this.exitoMsg = 'Curso creado correctamente';
        this.cargar();
      },
      error: (err) => {
        this.errorMsg = err?.status === 409 ? 'El curso ya existe' : 'Error al crear el curso';
      }
    });
  }

  // --- Modal: Asignar profesor jefe ---
  abrirModalProfesor(curso: any) {
    this.cursoSel = curso;
    this.profesorJefeRutSel = '';
    this.errorMsgProfesor = '';
    this.exitoMsg = '';
    this.showModalProfesor = true;
  }

  asignarProfesorJefe() {
    if (!this.profesorJefeRutSel) return;
    this.errorMsgProfesor = '';
    this.api.asignarProfesorJefe(this.cursoSel.id, { profesorJefeRut: this.profesorJefeRutSel }).subscribe({
      next: (resp) => {
        if (resp?.mensaje?.toLowerCase().includes('exitosamente')) {
          this.showModalProfesor = false;
          this.exitoMsg = 'Profesor jefe asignado correctamente';
          this.cargar();
        } else {
          this.errorMsgProfesor = resp?.mensaje || 'Error al asignar el profesor jefe';
        }
      },
      error: () => { this.errorMsgProfesor = 'Error al asignar el profesor jefe. Intenta nuevamente.'; }
    });
  }

  // --- Modal: Ver lista de alumnos ---
  abrirListaAlumnos(curso: any) {
    this.cursoSel = curso;
    this.listaAlumnosSel = [];
    this.showModalListaAlumnos = true;
    this.api.getAlumnosDeCurso(curso.id).subscribe({
      next: (alumnos) => { this.listaAlumnosSel = alumnos; },
      error: () => {}
    });
  }

  // --- Modal: Asignar alumno a curso ---
  abrirModalAlumno(curso: any) {
    this.cursoSel = curso;
    this.rutAlumnoInput = '';
    this.errorMsgAlumno = '';
    this.exitoMsg = '';
    this.showModalAlumno = true;
  }

  asignarAlumno() {
    if (!this.rutAlumnoInput.trim()) return;
    this.errorMsgAlumno = '';
    this.api.asignarAlumnoACurso(this.cursoSel.id, {
      alumnoRut: this.rutAlumnoInput.trim().toUpperCase()
    }).subscribe({
      next: (resp) => {
        if (resp?.mensaje?.toLowerCase().includes('exitosamente')) {
          this.showModalAlumno = false;
          this.exitoMsg = 'Alumno asignado al curso correctamente';
          this.cargar();
        } else {
          this.errorMsgAlumno = resp?.mensaje || 'Error al asignar el alumno. Verifica el RUT.';
        }
      },
      error: () => { this.errorMsgAlumno = 'Error al asignar el alumno. Verifica el RUT e intenta nuevamente.'; }
    });
  }
}
