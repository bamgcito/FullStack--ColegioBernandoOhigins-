import { Component, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { IonContent, IonIcon } from '@ionic/angular/standalone';
import { LayoutComponent } from '../../../shared/components/layout/layout.component';
import { addIcons } from 'ionicons';
import { personAddOutline, trashOutline, closeOutline } from 'ionicons/icons';
import { ApiService } from '../../../core/services/api.service';

const ROL_IDS: Record<string, number> = {
  ADMIN: 1, PROFESOR: 2, ALUMNO: 3, APODERADO: 4
};

@Component({
  selector: 'app-admin-usuarios',
  standalone: true,
  imports: [CommonModule, FormsModule, IonContent, IonIcon, LayoutComponent],
  templateUrl: './admin-usuarios.page.html',
  styleUrls: ['./admin-usuarios.page.scss'],
  host: { class: 'ion-page' }
})
export class AdminUsuariosPage implements OnInit {
  usuarios: any[] = [];
  filtroRol = 'TODOS';
  busqueda = '';
  showModal = false;
  paso = 1;
  loading = false;
  loadingPaso1 = false;
  loadingPaso2 = false;
  errorMsg = '';
  nuevo = {
    nombre: '', apellido: '', rut: '', password: '', rol: 'ALUMNO',
    fechaNacimiento: '', direccion: '', fechaMatricula: '',
    especialidad: '', correoInstitucional: '', telefono: '',
    correo: '', parentesco: ''
  };

  // Asociación
  showModalAsociacion = false;
  loadingAsociacion = false;
  errorAsociacion = '';
  exitoAsociacion = '';
  apoderadoSeleccionado: any = null;
  asociacion = { rutApoderado: '', rutAlumno: '', esPrincipal: true };

  get filtrados() {
    return this.usuarios.filter(u => {
      const rol = u.nombreRol || u.rol || '';
      const rolOk = this.filtroRol === 'TODOS' || rol === this.filtroRol;
      const busqOk = !this.busqueda
        || `${u.nombre || ''} ${u.apellido || ''}`.toLowerCase().includes(this.busqueda.toLowerCase())
        || (u.rut || '').includes(this.busqueda);
      return rolOk && busqOk;
    });
  }

  constructor(private api: ApiService) { addIcons({ personAddOutline, trashOutline, closeOutline }); }
  ngOnInit() { this.cargar(); }

  cargar() {
    this.loading = true;
    this.api.getUsuarios().subscribe({
      next: data => { this.usuarios = data; this.loading = false; },
      error: () => { this.errorMsg = 'Error al cargar usuarios'; this.loading = false; }
    });
  }

  badge(rol: string) {
    return { ADMIN: 'badge-primary', PROFESOR: 'badge-info', ALUMNO: 'badge-success', APODERADO: 'badge-warning' }[rol] || 'badge-neutral';
  }

  abrirModal() {
    this.paso = 1;
    this.errorMsg = '';
    this.nuevo = {
      nombre: '', apellido: '', rut: '', password: '', rol: 'ALUMNO',
      fechaNacimiento: '', direccion: '', fechaMatricula: '',
      especialidad: '', correoInstitucional: '', telefono: '',
      correo: '', parentesco: ''
    };
    this.showModal = true;
  }

  crearUsuario() {
    if (!this.nuevo.rut || !this.nuevo.password) {
      this.errorMsg = 'RUT y contraseña son obligatorios';
      return;
    }
    this.errorMsg = '';
    this.loadingPaso1 = true;
    const rolId = ROL_IDS[this.nuevo.rol] ?? 3;

    this.api.crearUsuario({ rut: this.nuevo.rut, contrasena: this.nuevo.password, rolId }, 'text').subscribe({
      next: (resp: any) => {
        this.loadingPaso1 = false;
        if (typeof resp === 'string' && resp.toLowerCase().includes('ya existe')) {
          this.errorMsg = resp;
          return;
        }
        if (this.nuevo.rol === 'ADMIN') {
          this.cerrarModal();
          this.cargar();
          return;
        }
        this.paso = 2;
      },
      error: (err) => {
        this.loadingPaso1 = false;
        if (err?.error instanceof ProgressEvent) {
          this.errorMsg = 'No se puede conectar al servidor';
        } else if (typeof err?.error === 'string') {
          this.errorMsg = err.error;
        } else {
          this.errorMsg = `Error ${err?.status || ''}: ${err?.error?.error || 'Error al crear usuario'}`;
        }
      }
    });
  }

  crearPerfil() {
    if (!this.nuevo.nombre || !this.nuevo.apellido) {
      this.errorMsg = 'Nombre y apellido son obligatorios';
      return;
    }
    this.errorMsg = '';
    this.loadingPaso2 = true;

    const base = { rut: this.nuevo.rut, nombre: this.nuevo.nombre, apellido: this.nuevo.apellido };
    const perfil = this.nuevo.rol === 'ALUMNO'
      ? { ...base, fechaNacimiento: this.nuevo.fechaNacimiento || null, direccion: this.nuevo.direccion || null, fechaMatricula: this.nuevo.fechaMatricula || null }
      : this.nuevo.rol === 'PROFESOR'
        ? { ...base, especialidad: this.nuevo.especialidad || null, correoInstitucional: this.nuevo.correoInstitucional || null, telefono: this.nuevo.telefono || null }
        : { ...base, telefono: this.nuevo.telefono || null, correo: this.nuevo.correo || null, parentesco: this.nuevo.parentesco || null };

    const perfil$ = this.nuevo.rol === 'ALUMNO' ? this.api.crearAlumno(perfil, 'text')
      : this.nuevo.rol === 'PROFESOR' ? this.api.crearProfesor(perfil, 'text')
        : this.api.crearApoderado(perfil, 'text');

    perfil$.subscribe({
      next: () => {
        this.loadingPaso2 = false;
        this.cerrarModal();
        this.cargar();
      },
      error: (err) => {
        this.loadingPaso2 = false;
        if (err?.error instanceof ProgressEvent) {
          this.errorMsg = 'No se puede conectar al servidor';
        } else if (typeof err?.error === 'string') {
          this.errorMsg = err.error;
        } else {
          this.errorMsg = `Error ${err?.status || ''}: ${err?.error?.error || 'Error al crear perfil'}`;
        }
      }
    });
  }

  cerrarModal() {
    this.showModal = false;
    this.paso = 1;
    this.errorMsg = '';
    this.nuevo = {
      nombre: '', apellido: '', rut: '', password: '', rol: 'ALUMNO',
      fechaNacimiento: '', direccion: '', fechaMatricula: '',
      especialidad: '', correoInstitucional: '', telefono: '',
      correo: '', parentesco: ''
    };
  }

  // ── ASOCIACIÓN ──────────────────────────────────────
  abrirAsociacion(usuario: any) {
    this.apoderadoSeleccionado = usuario;
    this.asociacion = { rutApoderado: usuario.rut, rutAlumno: '', esPrincipal: true };
    this.errorAsociacion = '';
    this.exitoAsociacion = '';
    this.showModalAsociacion = true;
  }

  asociar() {
    if (!this.asociacion.rutAlumno) {
      this.errorAsociacion = 'Ingresa el RUT del alumno';
      return;
    }
    this.errorAsociacion = '';
    this.exitoAsociacion = '';
    this.loadingAsociacion = true;

    this.api.asociarApoderado(this.asociacion, 'text').subscribe({
      next: (resp: any) => {
        this.loadingAsociacion = false;
        if (typeof resp === 'string' && resp.toLowerCase().includes('error')) {
          this.errorAsociacion = resp;
        } else {
          this.exitoAsociacion = typeof resp === 'string' ? resp : 'Asociación realizada correctamente';
          this.asociacion.rutAlumno = '';
        }
      },
      error: (err) => {
        this.loadingAsociacion = false;
        if (err?.error instanceof ProgressEvent) {
          this.errorAsociacion = 'No se puede conectar al servidor';
        } else if (typeof err?.error === 'string') {
          this.errorAsociacion = err.error;
        } else {
          this.errorAsociacion = 'Error al asociar apoderado';
        }
      }
    });
  }

  cerrarAsociacion() {
    this.showModalAsociacion = false;
    this.apoderadoSeleccionado = null;
    this.errorAsociacion = '';
    this.exitoAsociacion = '';
  }

  eliminar(id: number) {
    if (id === 1) { alert('No puedes eliminar al admin.'); return; }
    this.usuarios = this.usuarios.filter(u => u.id !== id);
  }
}