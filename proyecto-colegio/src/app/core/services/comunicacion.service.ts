import { Injectable, OnDestroy } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable, Subject } from 'rxjs';
import { io, Socket } from 'socket.io-client';
import { AuthService } from './auth.service';
import { environment } from '../../../environments/environment';

@Injectable({ providedIn: 'root' })
export class ComunicacionService implements OnDestroy {
  private readonly httpBase = environment.apiUrl;
  private readonly socketUrl = environment.apiUrl.replace(/:\d+$/, ':3000');

  private socket: Socket | null = null;
  nuevoMensaje$ = new Subject<any>();
  nuevaNotificacion$ = new Subject<any>();

  constructor(private http: HttpClient, private auth: AuthService) { }

  // ── SOCKET.IO 

  conectar(usuarioId: number): void {
    if (this.socket?.connected) return;
    this.socket = io(this.socketUrl, { transports: ['websocket', 'polling'] });

    this.socket.on('connect', () => {
      this.socket!.emit('unirse', usuarioId);
    });

    this.socket.on('nuevo_mensaje', (msg: any) => {
      this.nuevoMensaje$.next(msg);
    });

    this.socket.on('nueva_notificacion', (notif: any) => {
      this.nuevaNotificacion$.next(notif);
    });
  }

  desconectar(): void {
    this.socket?.disconnect();
    this.socket = null;
  }

  // ── CONVERSACIONES ────────────────────────────────────────────────────

  getConversacionesUsuario(usuarioId: number): Observable<any> {
    return this.http.get<any>(`${this.httpBase}/bff/ms/comunicacion/conversaciones/usuario/${usuarioId}`);
  }

  crearConversacion(data: {
    iniciador_id: number;
    iniciador_tipo: 'APODERADO' | 'ALUMNO' | 'PROFESOR';
    receptor_id: number;
    receptor_tipo: 'PROFESOR_JEFE' | 'PROFESOR_ASIGNATURA' | 'APODERADO';
    alumno_id: number;
    curso_id: number;
  }): Observable<any> {
    return this.http.post<any>(`${this.httpBase}/bff/ms/comunicacion/conversaciones`, data);
  }

  // ── MENSAJES ──────────────────────────────────────────────────────────

  getMensajes(conversacionId: number): Observable<any> {
    return this.http.get<any>(`${this.httpBase}/bff/ms/comunicacion/mensajes/conversacion/${conversacionId}`);
  }

  enviarMensaje(data: {
    conversacion_id: number;
    emisor_id: number;
    emisor_tipo: 'APODERADO' | 'PROFESOR' | 'ALUMNO';
    contenido: string;
  }): Observable<any> {
    return this.http.post<any>(`${this.httpBase}/bff/ms/comunicacion/mensajes`, data);
  }

  // ── NOTIFICACIONES ────────────────────────────────────────────────────

  getNotificaciones(usuarioId: number): Observable<any> {
    return this.http.get<any>(`${this.httpBase}/bff/ms/comunicacion/notificaciones/usuario/${usuarioId}`);
  }

  marcarLeida(notificacionId: number): Observable<any> {
    return this.http.patch<any>(`${this.httpBase}/bff/ms/comunicacion/notificaciones/${notificacionId}/leer`, {});
  }

  ngOnDestroy(): void {
    this.desconectar();
  }
}
