import { Component, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { IonContent, IonIcon } from '@ionic/angular/standalone';
import { LayoutComponent } from '../../../shared/components/layout/layout.component';
import { addIcons } from 'ionicons';
import { personAddOutline, trashOutline, closeOutline } from 'ionicons/icons';
import { ApiService } from '../../../core/services/api.service';
import { forkJoin } from 'rxjs';
import { catchError } from 'rxjs/operators';
import { of } from 'rxjs';

const ROL_IDS: Record<string, number> = {
  ADMIN: 1, PROFESOR: 2, ALUMNO: 3, APODERADO: 4
};

@Component({
  selector: 'app-admin-usuarios',
  standalone: true,
  imports: [CommonModule, FormsModule, IonContent, IonIcon, LayoutComponent],
  templateUrl: './admin-usuarios.page.html',
  styleUrls: ['./admin-usuarios.page.scss']
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
  exitoMsg = '';
  nuevo = {
    nombre: '', apellido: '', rut: '', password: '', rol: 'ALUMNO',
    fechaNacimiento: '', direccion: '', fechaMatricula: '', edad: null as number | null,
    especialidad: '', correoInstitucional: '', telefono: '',
    correo: '', parentesco: '', telefonoEmergencia: '', fechaContrato: ''
  };

  showModalAsociacion = false;
  loadingAsociacion = false;
  errorAsociacion = '';
  exitoAsociacion = '';
  apoderadoSeleccionado: any = null;
  asociacion = { rutApoderado: '', rutAlumno: '', esPrincipal: true };
  alumnosAsociados: any[] = [];
  loadingAlumnosAsociados = false;

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
      next: data => {
        this.usuarios = data;
        this.loading = false;
        const alumnoUsers = data.filter((u: any) => (u.nombreRol || u.rol) === 'ALUMNO');
        const profUsers = data.filter((u: any) => (u.nombreRol || u.rol) === 'PROFESOR');
        const apoderadoUsers = data.filter((u: any) => (u.nombreRol || u.rol) === 'APODERADO');
        const calls = [
          ...alumnoUsers.map((u: any) => this.api.getPerfilAlumno(u.id).pipe(catchError(() => of(null)))),
          ...profUsers.map((u: any) => this.api.getPerfilProfesor(u.id).pipe(catchError(() => of(null)))),
          ...apoderadoUsers.map((u: any) => this.api.getPerfilApoderado(u.id).pipe(catchError(() => of(null))))
        ];
        if (!calls.length) return;
        forkJoin(calls).subscribe({
          next: perfiles => {
            const mapa: Record<string, { nombre: string; apellido: string }> = {};
            perfiles.forEach((p: any) => { if (p?.rut) mapa[p.rut] = { nombre: p.nombre, apellido: p.apellido }; });
            this.usuarios = this.usuarios.map((u: any) => {
              const p = mapa[u.rut];
              return p ? { ...u, nombre: p.nombre, apellido: p.apellido } : u;
            });
          }
        });
      },
      error: () => { this.errorMsg = 'Error al cargar usuarios'; this.loading = false; }
    });
  }

  badge(rol: string) {
    return { ADMIN: 'badge-primary', PROFESOR: 'badge-info', ALUMNO: 'badge-success', APODERADO: 'badge-warning' }[rol] || 'badge-neutral';
  }

  abrirModal() {
    this.paso = 1;
    this.errorMsg = '';
    this.exitoMsg = '';
    this.nuevo = {
      nombre: '', apellido: '', rut: '', password: '', rol: 'ALUMNO',
      fechaNacimiento: '', direccion: '', fechaMatricula: '', edad: null,
      especialidad: '', correoInstitucional: '', telefono: '',
      correo: '', parentesco: '', telefonoEmergencia: '', fechaContrato: ''
    };
    this.showModal = true;
  }

  private parseMensaje(resp: any): string {
    try {
      const p = typeof resp === 'string' ? JSON.parse(resp) : resp;
      return p?.mensaje ?? String(resp);
    } catch { return String(resp); }
  }

  crearUsuario() {
    if (!this.nuevo.rut || !this.nuevo.password) {
      this.errorMsg = 'RUT y contraseña son obligatorios';
      return;
    }
    this.errorMsg = '';
    this.loadingPaso1 = true;
    const rut = this.nuevo.rut.trim().toUpperCase();
    const rolId = ROL_IDS[this.nuevo.rol] ?? 3;

    this.api.crearUsuario({ rut, contrasena: this.nuevo.password, rolId }).subscribe({
      next: (resp: any) => {
        this.loadingPaso1 = false;
        const msg = this.parseMensaje(resp);
        if (msg.toLowerCase().includes('ya existe')) {
          this.errorMsg = msg;
          return;
        }
        if (this.nuevo.rol === 'ADMIN') {
          this.cerrarModal();
          this.exitoMsg = 'Administrador creado correctamente';
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
          this.errorMsg = 'No se pudo crear el usuario. Revisa los datos ingresados.';
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

    const base = { rut: this.nuevo.rut.trim().toUpperCase(), nombre: this.nuevo.nombre.trim(), apellido: this.nuevo.apellido.trim() };
    const perfil = this.nuevo.rol === 'ALUMNO'
      ? { ...base, fechaNacimiento: this.nuevo.fechaNacimiento || null, direccion: this.nuevo.direccion?.trim() || null, fechaMatricula: this.nuevo.fechaMatricula || null, edad: this.nuevo.edad || null }
      : this.nuevo.rol === 'PROFESOR'
        ? { ...base, especialidad: this.nuevo.especialidad?.trim() || null, correoInstitucional: this.nuevo.correoInstitucional?.trim() || null, telefono: this.nuevo.telefono?.trim() || null, fechaNacimiento: this.nuevo.fechaNacimiento || null, fechaContrato: this.nuevo.fechaContrato || null }
        : { ...base, telefono: this.nuevo.telefono?.trim() || null, correo: this.nuevo.correo?.trim() || null, parentesco: this.nuevo.parentesco?.trim() || null, telefonoEmergencia: this.nuevo.telefonoEmergencia?.trim() || null, direccion: this.nuevo.direccion?.trim() || null, fechaNacimiento: this.nuevo.fechaNacimiento || null };

    const perfil$ = this.nuevo.rol === 'ALUMNO' ? this.api.crearPerfilAlumno(perfil)
      : this.nuevo.rol === 'PROFESOR' ? this.api.crearPerfilProfesor(perfil)
        : this.api.crearPerfilApoderado(perfil);

    perfil$.subscribe({
      next: () => {
        this.loadingPaso2 = false;
        this.cerrarModal();
        this.exitoMsg = 'Usuario creado correctamente';
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
      fechaNacimiento: '', direccion: '', fechaMatricula: '', edad: null,
      especialidad: '', correoInstitucional: '', telefono: '',
      correo: '', parentesco: '', telefonoEmergencia: '', fechaContrato: ''
    };
  }

  abrirAsociacion(usuario: any) {
    this.apoderadoSeleccionado = usuario;
    this.asociacion = { rutApoderado: usuario.rut, rutAlumno: '', esPrincipal: true };
    this.errorAsociacion = '';
    this.exitoAsociacion = '';
    this.alumnosAsociados = [];
    this.showModalAsociacion = true;
    this.cargarAlumnosAsociados();
  }

  private cargarAlumnosAsociados() {
    if (!this.apoderadoSeleccionado?.id) return;
    this.loadingAlumnosAsociados = true;
    this.api.getAlumnosDeApoderado(this.apoderadoSeleccionado.id).subscribe({
      next: (data) => { this.alumnosAsociados = data || []; this.loadingAlumnosAsociados = false; },
      error: () => { this.alumnosAsociados = []; this.loadingAlumnosAsociados = false; }
    });
  }

  asociar() {
    if (!this.asociacion.rutAlumno.trim()) {
      this.errorAsociacion = 'Ingresa el RUT del alumno';
      return;
    }
    this.errorAsociacion = '';
    this.exitoAsociacion = '';
    this.loadingAsociacion = true;

    const payload = {
      rutAlumno: this.asociacion.rutAlumno.trim().toUpperCase(),
      rutApoderado: this.asociacion.rutApoderado.trim().toUpperCase(),
      esPrincipal: this.asociacion.esPrincipal
    };

    this.api.asociarAlumnoApoderado(payload).subscribe({
      next: (resp: any) => {
        this.loadingAsociacion = false;
        const msg = this.parseMensaje(resp);
        if (msg.toLowerCase().includes('error') || msg.toLowerCase().includes('no existe') || msg.toLowerCase().includes('ya está')) {
          this.errorAsociacion = msg;
        } else {
          this.exitoAsociacion = msg || 'Asociación realizada correctamente';
          this.asociacion.rutAlumno = '';
          this.cargarAlumnosAsociados();
        }
      },
      error: (err) => {
        this.loadingAsociacion = false;
        if (err?.error instanceof ProgressEvent) {
          this.errorAsociacion = 'No se puede conectar al servidor';
        } else if (typeof err?.error === 'string') {
          this.errorAsociacion = err.error;
        } else {
          this.errorAsociacion = 'Error al asociar apoderado. Verifica el RUT e intenta nuevamente.';
        }
      }
    });
  }

  cerrarAsociacion() {
    this.showModalAsociacion = false;
    this.apoderadoSeleccionado = null;
    this.errorAsociacion = '';
    this.exitoAsociacion = '';
    this.alumnosAsociados = [];
    this.loadingAlumnosAsociados = false;
  }

  eliminar(id: number) {
    if (id === 1) { this.errorMsg = 'No puedes eliminar al administrador principal'; return; }
    if (!confirm('¿Estás seguro de eliminar este usuario?')) return;
    this.exitoMsg = '';
    this.errorMsg = '';
    const usuario = this.usuarios.find(u => u.id === id);
    const rol = (usuario?.nombreRol || usuario?.rol || '').toUpperCase();
    const perfil$ = rol === 'ALUMNO' ? this.api.eliminarPerfilAlumno(id)
      : rol === 'PROFESOR' ? this.api.eliminarPerfilProfesor(id)
      : rol === 'APODERADO' ? this.api.eliminarPerfilApoderado(id)
      : null;
    const borrar = () => this.api.eliminarUsuario(id).subscribe({
      next: () => { this.exitoMsg = 'Usuario eliminado correctamente'; this.cargar(); },
      error: () => { this.errorMsg = 'Error al eliminar el usuario. Intenta nuevamente.'; }
    });
    if (perfil$) {
      perfil$.pipe(catchError(() => of(null))).subscribe(() => borrar());
    } else {
      borrar();
    }
  }
}
