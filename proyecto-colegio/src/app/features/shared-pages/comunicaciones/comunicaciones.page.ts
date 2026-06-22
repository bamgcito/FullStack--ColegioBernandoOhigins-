import { Component, OnInit, OnDestroy } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { IonContent, IonIcon } from '@ionic/angular/standalone';
import { LayoutComponent } from '../../../shared/components/layout/layout.component';
import { addIcons } from 'ionicons';
import { sendOutline, chatbubblesOutline, addOutline, closeOutline, arrowBackOutline } from 'ionicons/icons';
import { AuthService } from '../../../core/services/auth.service';
import { ApiService } from '../../../core/services/api.service';
import { ComunicacionService } from '../../../core/services/comunicacion.service';
import { Subscription, forkJoin } from 'rxjs';
import { catchError, of, timeout } from 'rxjs';

@Component({
  selector: 'app-comunicaciones',
  standalone: true,
  imports: [CommonModule, FormsModule, IonContent, IonIcon, LayoutComponent],
  templateUrl: './comunicaciones.page.html',
  styleUrls: ['./comunicaciones.page.scss']
})
export class ComunicacionesPage implements OnInit, OnDestroy {
  userId = 0;
  rol = '';
  conversaciones: any[] = [];
  activa: any = null;
  mensajes: any[] = [];
  nuevoMensaje = '';
  cargando = false;
  errorMsg = '';
  mostrarChat = false;

  // Panel nueva conversación (apoderado / alumno)
  showNueva = false;
  alumnos: any[] = [];
  alumnoSeleccionado: any = null;
  cursosAlumno: any[] = [];
  cursoSeleccionado: any = null;
  profesoresDisponibles: any[] = [];
  profesorSeleccionado: any = null;
  tipoReceptor: 'PROFESOR_JEFE' | 'PROFESOR_ASIGNATURA' = 'PROFESOR_JEFE';
  creandoConv = false;
  errorNueva = '';

  // Panel nueva conversación (profesor → apoderado)
  cursosProfesor: any[] = [];
  alumnosDelCurso: any[] = [];
  alumnoElegido: any = null;
  apoderadosDelAlumno: any[] = [];
  apoderadoElegido: any = null;

  private subs: Subscription[] = [];

  constructor(
    private auth: AuthService,
    private api: ApiService,
    private comunicacion: ComunicacionService
  ) {
    addIcons({ sendOutline, chatbubblesOutline, addOutline, closeOutline, arrowBackOutline });
  }

  ngOnInit() {
    const user = this.auth.currentUser;
    if (!user) return;
    this.userId = user.id;
    this.rol = user.rol;

    this.comunicacion.conectar(this.userId);

    const subMsg = this.comunicacion.nuevoMensaje$.subscribe(msg => {
      if (this.activa && msg.conversacion_id === this.activa.id) {
        this.mensajes = [...this.mensajes, msg];
        this.scrollAbajo();
      } else {
        const conv = this.conversaciones.find(c => c.id === msg.conversacion_id);
        if (conv) {
          conv.sinLeer = (conv.sinLeer || 0) + 1;
          conv.ultimoMensaje = msg.contenido;
        }
      }
    });
    this.subs.push(subMsg);

    this.cargarConversaciones();

    if (this.rol === 'APODERADO') {
      this.api.getAlumnosDeApoderado(this.userId).pipe(catchError(() => of([]))).subscribe(a => {
        this.alumnos = a || [];
      });
    }

    if (this.rol === 'PROFESOR') {
      this.cargarCursosProfesor();
    }
  }

  puedeIniciar(): boolean { return this.rol === 'APODERADO' || this.rol === 'ALUMNO' || this.rol === 'PROFESOR'; }

  cargarConversaciones() {
    this.cargando = true;
    this.errorMsg = '';

    this.comunicacion.getConversacionesUsuario(this.userId).pipe(
      timeout(10000),
      catchError(err => {
        this.errorMsg = err.name === 'TimeoutError'
          ? 'El servidor de mensajería no respondió a tiempo.'
          : 'No se pudieron cargar las conversaciones.';
        return of({ lista_conversaciones: [] });
      })
    ).subscribe({
      next: (resp: any) => {
        const lista = resp?.respuesta?.lista_conversaciones ?? [];
        this.conversaciones = lista.map((c: any) => ({ ...c, sinLeer: 0, ultimoMensaje: c.ultimo_mensaje || '' }));
        this.cargando = false;
      }
    });
  }

  volverLista() { this.mostrarChat = false; }

  seleccionar(c: any) {
    this.activa = c;
    this.mostrarChat = true;
    c.sinLeer = 0;
    this.mensajes = [];
    this.comunicacion.getMensajes(c.id).pipe(catchError(() => of(null))).subscribe({
      next: (resp: any) => {
        this.mensajes = resp?.respuesta?.lista_mensajes ?? [];
        this.scrollAbajo();
      }
    });
  }

  enviar() {
    if (!this.nuevoMensaje.trim() || !this.activa) return;
    const emisorTipo: 'APODERADO' | 'PROFESOR' | 'ALUMNO' =
      this.rol === 'APODERADO' ? 'APODERADO' : this.rol === 'ALUMNO' ? 'ALUMNO' : 'PROFESOR';
    const payload = {
      conversacion_id: this.activa.id,
      emisor_id: this.userId,
      emisor_tipo: emisorTipo,
      contenido: this.nuevoMensaje.trim()
    };
    const texto = this.nuevoMensaje.trim();
    this.nuevoMensaje = '';

    this.comunicacion.enviarMensaje(payload).pipe(catchError(() => of(null))).subscribe({
      next: (resp: any) => {
        const msg = resp?.respuesta?.mensaje ?? { conversacion_id: this.activa.id, emisor_id: this.userId, contenido: texto, fecha_envio: new Date().toISOString() };
        this.mensajes = [...this.mensajes, msg];
        if (this.activa) this.activa.ultimoMensaje = texto;
        this.scrollAbajo();
      }
    });
  }

  // ── NUEVA CONVERSACIÓN (apoderado / alumno / profesor) ─────────────────

  private cargarCursosProfesor() {
    this.api.getCursos().pipe(catchError(() => of([]))).subscribe(cursos => {
      forkJoin((cursos as any[]).map((c: any) => this.api.getAsignacionesPorCurso(c.id).pipe(catchError(() => of([]))))).subscribe((res: any[]) => {
        const cursosConAsignacion = new Set(
          res.flat().filter((a: any) => a.profesorId === this.userId).map((a: any) => a.cursoId)
        );
        this.cursosProfesor = (cursos as any[]).filter((c: any) => c.profesorJefeId === this.userId || cursosConAsignacion.has(c.id));
      });
    });
  }

  abrirNueva() {
    this.showNueva = true;
    this.errorNueva = '';
    this.alumnoSeleccionado = null;
    this.cursoSeleccionado = null;
    this.cursosAlumno = [];
    this.profesoresDisponibles = [];
    this.profesorSeleccionado = null;
    this.tipoReceptor = 'PROFESOR_JEFE';
    this.alumnosDelCurso = [];
    this.alumnoElegido = null;
    this.apoderadosDelAlumno = [];
    this.apoderadoElegido = null;

    if (this.rol === 'ALUMNO') {
      this.api.getCursoDeAlumno(this.userId).pipe(catchError(() => of(null))).subscribe(curso => {
        if (!curso) return;
        this.cursoSeleccionado = curso;
        this.cargarProfesoresCurso(curso);
      });
    }
  }

  onCursoProfesorChange(curso: any) {
    this.cursoSeleccionado = curso;
    this.alumnosDelCurso = [];
    this.alumnoElegido = null;
    this.apoderadosDelAlumno = [];
    this.apoderadoElegido = null;
    if (!curso) return;

    this.api.getAlumnosDeCurso(curso.id).pipe(catchError(() => of([]))).subscribe((alumnos: any[]) => {
      this.alumnosDelCurso = (alumnos || []).filter((a: any) => a.nombre);
    });
  }

  onAlumnoProfesorChange(alumno: any) {
    this.alumnoElegido = alumno;
    this.apoderadosDelAlumno = [];
    this.apoderadoElegido = null;
    if (!alumno) return;

    this.api.getApoderadosDeAlumno(alumno.alumnoId).pipe(catchError(() => of([]))).subscribe((apoderados: any[]) => {
      this.apoderadosDelAlumno = apoderados || [];
      if (this.apoderadosDelAlumno.length === 1) this.apoderadoElegido = this.apoderadosDelAlumno[0];
    });
  }

  cerrarNueva() { this.showNueva = false; }

  onAlumnoChange(alumno: any) {
    this.alumnoSeleccionado = alumno;
    this.cursoSeleccionado = null;
    this.profesoresDisponibles = [];
    this.profesorSeleccionado = null;
    this.cursosAlumno = [];
    if (!alumno) return;

    this.api.getCursos().pipe(catchError(() => of([]))).subscribe(cursos => {
      const cargarAlumnos = cursos.map((c: any) =>
        this.api.getAlumnosDeCurso(c.id).pipe(catchError(() => of([]))).subscribe((alumnos: any[]) => {
          const encontrado = alumnos.some((a: any) => String(a.alumnoId) === String(alumno.usuarioId));
          if (encontrado) this.cursosAlumno = [...this.cursosAlumno, c];
        })
      );
    });
  }

  onCursoChange(curso: any) {
    this.cursoSeleccionado = curso;
    this.profesorSeleccionado = null;
    this.profesoresDisponibles = [];
    if (!curso) return;
    this.cargarProfesoresCurso(curso);
  }

  onTipoChange() {
    this.profesorSeleccionado = null;
    if (this.cursoSeleccionado) this.cargarProfesoresCurso(this.cursoSeleccionado);
  }

  private cargarProfesoresCurso(curso: any) {
    if (this.tipoReceptor === 'PROFESOR_JEFE') {
      if (curso.profesorJefeId) {
        this.api.getPerfilProfesor(curso.profesorJefeId).pipe(catchError(() => of(null))).subscribe(prof => {
          this.profesoresDisponibles = prof ? [{ ...prof, id: curso.profesorJefeId }] : [];
        });
      } else {
        this.profesoresDisponibles = [];
      }
    } else {
      this.api.getAsignacionesPorCurso(curso.id).pipe(catchError(() => of([]))).subscribe(asignaciones => {
        const profIds = [...new Set((asignaciones as any[]).map((a: any) => a.profesorId))];
        this.profesoresDisponibles = [];
        profIds.forEach(pid => {
          this.api.getPerfilProfesor(pid).pipe(catchError(() => of(null))).subscribe(prof => {
            if (prof) this.profesoresDisponibles = [...this.profesoresDisponibles, { ...prof, id: pid }];
          });
        });
      });
    }
  }

  crearConversacion() {
    if (this.rol === 'PROFESOR') {
      if (!this.cursoSeleccionado || !this.alumnoElegido || !this.apoderadoElegido) {
        this.errorNueva = 'Completa todos los campos';
        return;
      }
      this.creandoConv = true;
      this.errorNueva = '';

      this.comunicacion.crearConversacion({
        iniciador_id: this.userId,
        iniciador_tipo: 'PROFESOR',
        receptor_id: this.apoderadoElegido.usuarioId,
        receptor_tipo: 'APODERADO',
        alumno_id: this.alumnoElegido.alumnoId,
        curso_id: this.cursoSeleccionado.id
      }).pipe(catchError(err => {
        this.errorNueva = err.error?.mensaje || 'Error al crear conversación';
        this.creandoConv = false;
        return of(null);
      })).subscribe({
        next: (resp: any) => {
          this.creandoConv = false;
          if (!resp) return;
          this.showNueva = false;
          this.cargarConversaciones();
        }
      });
      return;
    }

    const esApoderado = this.rol === 'APODERADO';
    if (esApoderado && (!this.alumnoSeleccionado || !this.cursoSeleccionado || !this.profesorSeleccionado)) {
      this.errorNueva = 'Completa todos los campos';
      return;
    }
    if (!esApoderado && (!this.cursoSeleccionado || !this.profesorSeleccionado)) {
      this.errorNueva = 'Completa todos los campos';
      return;
    }
    this.creandoConv = true;
    this.errorNueva = '';

    this.comunicacion.crearConversacion({
      iniciador_id: this.userId,
      iniciador_tipo: esApoderado ? 'APODERADO' : 'ALUMNO',
      receptor_id: this.profesorSeleccionado.id,
      receptor_tipo: this.tipoReceptor,
      alumno_id: esApoderado ? this.alumnoSeleccionado.id : this.userId,
      curso_id: this.cursoSeleccionado.id
    }).pipe(catchError(err => {
      this.errorNueva = err.error?.mensaje || 'Error al crear conversación';
      this.creandoConv = false;
      return of(null);
    })).subscribe({
      next: (resp: any) => {
        this.creandoConv = false;
        if (!resp) return;
        this.showNueva = false;
        this.cargarConversaciones();
      }
    });
  }

  // ── HELPERS ───────────────────────────────────────────────────────────

  esMio(m: any): boolean { return m.emisor_id === this.userId; }

  hora(f: string): string {
    if (!f) return '';
    return new Date(f).toLocaleTimeString('es-CL', { hour: '2-digit', minute: '2-digit' });
  }

  fechaCorta(f?: string): string {
    if (!f) return '';
    return new Date(f).toLocaleDateString('es-CL', { day: '2-digit', month: 'short' });
  }

  soyIniciador(c: any): boolean { return String(c.iniciador_id) === String(this.userId); }

  nombreOtro(c: any): string {
    if (this.soyIniciador(c)) {
      return c.receptor_nombre || (c.receptor_tipo === 'APODERADO' ? `Apoderado #${c.receptor_id}` : `Profesor #${c.receptor_id}`);
    }
    if (c.iniciador_tipo === 'ALUMNO') return c.iniciador_nombre || `Alumno #${c.iniciador_id}`;
    if (c.iniciador_tipo === 'PROFESOR') return c.iniciador_nombre || `Profesor #${c.iniciador_id}`;
    return c.iniciador_nombre || `Apoderado #${c.iniciador_id}`;
  }

  otroTipo(c: any): string {
    return this.soyIniciador(c) ? (c.receptor_tipo || '') : (c.iniciador_tipo || '');
  }

  inicialesOtro(c: any): string {
    const nombre = this.nombreOtro(c);
    const partes = nombre.split(' ');
    return partes.length >= 2 ? `${partes[0][0]}${partes[1][0]}` : nombre[0] || '?';
  }

  private scrollAbajo() {
    setTimeout(() => {
      const el = document.querySelector('.mensajes-lista');
      if (el) el.scrollTop = el.scrollHeight;
    }, 60);
  }

  ngOnDestroy() {
    this.subs.forEach(s => s.unsubscribe());
    this.comunicacion.desconectar();
  }
}
