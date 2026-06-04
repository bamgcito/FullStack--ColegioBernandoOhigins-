import { Component, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { IonContent, IonIcon } from '@ionic/angular/standalone';
import { LayoutComponent } from '../../../shared/components/layout/layout.component';
import { addIcons } from 'ionicons';
import { sendOutline, chatbubblesOutline } from 'ionicons/icons';
import { AuthService } from '../../../core/services/auth.service';
import * as Mock from '../../../shared/mocks/mock-data';

@Component({
  selector: 'app-comunicaciones',
  standalone: true,
  imports: [CommonModule, FormsModule, IonContent, IonIcon, LayoutComponent],
  templateUrl: './comunicaciones.page.html',
  styleUrls: ['./comunicaciones.page.scss']
})
export class ComunicacionesPage implements OnInit {
  userId = 0;
  conversaciones: any[] = [];
  activa: any = null;
  mensajes: Mock.Mensaje[] = [];
  nuevoMensaje = '';

  constructor(private auth: AuthService) { addIcons({ sendOutline, chatbubblesOutline }); }

  ngOnInit() {
    this.userId = this.auth.currentUser?.id ?? 0;
    this.conversaciones = Mock.CONVERSACIONES_MOCK
      .filter(c => c.participantesIds.includes(this.userId))
      .map(c => ({ ...c, otro: this.getOtro(c), sinLeer: Mock.MENSAJES_MOCK.filter(m => m.conversacionId === c.id && !m.leido && m.emisorId !== this.userId).length }));
  }

  getOtro(c: Mock.Conversacion) {
    const id = c.participantesIds.find(i => i !== this.userId);
    const u = Mock.USUARIOS_MOCK.find(u => u.id === id);
    return u ? { ...u, iniciales: `${u.nombre[0]}${u.apellido[0]}` } : null;
  }

  seleccionar(c: any) {
    this.activa = c;
    this.mensajes = [...Mock.MENSAJES_MOCK.filter(m => m.conversacionId === c.id)].sort((a, b) => a.fecha.localeCompare(b.fecha));
    c.sinLeer = 0;
    setTimeout(() => { const el = document.querySelector('.mensajes-lista'); if (el) el.scrollTop = el.scrollHeight; }, 60);
  }

  enviar() {
    if (!this.nuevoMensaje.trim() || !this.activa) return;
    const msg: Mock.Mensaje = { id: Date.now(), conversacionId: this.activa.id, emisorId: this.userId, contenido: this.nuevoMensaje.trim(), fecha: new Date().toISOString(), leido: false };
    this.mensajes.push(msg);
    this.activa.ultimoMensaje = msg.contenido;
    this.nuevoMensaje = '';
    setTimeout(() => { const el = document.querySelector('.mensajes-lista'); if (el) el.scrollTop = el.scrollHeight; }, 60);
  }

  esMio(m: Mock.Mensaje) { return m.emisorId === this.userId; }
  hora(f: string) { return new Date(f).toLocaleTimeString('es-CL', { hour: '2-digit', minute: '2-digit' }); }
  fechaCorta(f?: string) { if (!f) return ''; return new Date(f).toLocaleDateString('es-CL', { day: '2-digit', month: 'short' }); }
}
