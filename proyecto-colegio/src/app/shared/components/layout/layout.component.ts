import { Component, Input, OnInit, OnDestroy, NgZone } from '@angular/core';
import { CommonModule } from '@angular/common';
import { Router } from '@angular/router';
import { IonIcon } from '@ionic/angular/standalone';
import { addIcons } from 'ionicons';
import {
  gridOutline, peopleOutline, schoolOutline, bookOutline, clipboardOutline,
  documentTextOutline, starOutline, checkmarkCircleOutline, pencilOutline,
  chatbubblesOutline, personOutline, menuOutline, logOutOutline, notificationsOutline,
  calendarOutline
} from 'ionicons/icons';
import { AuthService, AuthUser } from '../../../core/services/auth.service';
import { ComunicacionService } from '../../../core/services/comunicacion.service';
import { Subscription, Subject } from 'rxjs';
import { catchError, of, switchMap } from 'rxjs';

@Component({
  selector: 'app-layout',
  standalone: true,
  imports: [CommonModule, IonIcon],
  templateUrl: './layout.component.html',
  styleUrls: ['./layout.component.scss']
})
export class LayoutComponent implements OnInit, OnDestroy {
  @Input() pageTitle = 'Dashboard';
  currentUser: AuthUser | null = null;
  sidebarOpen = false;

  notificaciones: any[] = [];
  mostrarNotif = false;
  private subs: Subscription[] = [];
  private recargarNotif$ = new Subject<void>();

  constructor(
    private auth: AuthService,
    private router: Router,
    private ngZone: NgZone,
    private comunicacion: ComunicacionService
  ) {
    addIcons({
      gridOutline, peopleOutline, schoolOutline, bookOutline, clipboardOutline,
      documentTextOutline, starOutline, checkmarkCircleOutline, pencilOutline,
      chatbubblesOutline, personOutline, menuOutline, logOutOutline, notificationsOutline,
      calendarOutline
    });
  }

  get initials(): string {
    if (!this.currentUser) return '?';
    const n = this.currentUser.nombre?.[0] || '?';
    const a = this.currentUser.apellido?.[0] || '';
    return `${n}${a}`.toUpperCase();
  }
  get rolLabel(): string {
    const labels: Record<string, string> = {
      ADMIN: 'Administrador', PROFESOR: 'Profesor',
      ALUMNO: 'Alumno', APODERADO: 'Apoderado'
    };
    return labels[this.currentUser?.rol || ''] || '';
  }
  get today(): string {
    return new Date().toLocaleDateString('es-CL', {
      weekday: 'long', day: '2-digit', month: 'long', year: 'numeric'
    });
  }

get noLeidas(): number { return this.notificaciones.filter(n => !n.leido).length; }

  ngOnInit() {
    this.currentUser = this.auth.currentUser;
    if (!this.currentUser) return;

    this.comunicacion.conectar(this.currentUser.id);

    const subRecargar = this.recargarNotif$.pipe(
      switchMap(() =>
        this.comunicacion.getNotificaciones(this.currentUser!.id).pipe(catchError(() => of(null)))
      )
    ).subscribe(resp => {
      this.ngZone.run(() => {
        const todas = resp?.respuesta?.lista_notificaciones ?? [];
        const descartadas = this.getDescartadas();
        this.notificaciones = todas.filter((n: any) => !descartadas.has(n.id));
      });
    });
    this.subs.push(subRecargar);

    this.recargarNotif$.next();

    const sub = this.comunicacion.nuevaNotificacion$.subscribe(n => {
      this.ngZone.run(() => {
        if (this.router.url.includes('/comunicaciones')) {
          this.comunicacion.marcarLeida(n.id).pipe(catchError(() => of(null))).subscribe();
        }
        this.recargarNotif$.next();
      });
    });
    this.subs.push(sub);
  }

  private get descartadasKey(): string {
    return `notif_descartadas_${this.currentUser?.id ?? 0}`;
  }

  private getDescartadas(): Set<number> {
    try {
      const stored = localStorage.getItem(this.descartadasKey);
      return new Set(stored ? JSON.parse(stored) : []);
    } catch { return new Set(); }
  }

  private saveDescartadas(set: Set<number>) {
    localStorage.setItem(this.descartadasKey, JSON.stringify([...set]));
  }

  toggleNotif() { this.mostrarNotif = !this.mostrarNotif; }
  cerrarNotif() { this.mostrarNotif = false; }

  descartarNotificacion(n: any, event: MouseEvent) {
    event.stopPropagation();
    this.notificaciones = this.notificaciones.filter(x => x !== n);
    const descartadas = this.getDescartadas();
    descartadas.add(n.id);
    this.saveDescartadas(descartadas);
  }

  abrirNotificacion(n: any) {
    this.mostrarNotif = false;
    if (!n.leido) {
      n.leido = true;
      this.comunicacion.marcarLeida(n.id).pipe(catchError(() => of(null))).subscribe();
    }
    const rol = this.currentUser?.rol?.toLowerCase();
    if (rol) this.router.navigate([`/${rol}/comunicaciones`]);
  }

  isActive(ruta: string): boolean {
    return this.router.url === ruta || this.router.url.startsWith(ruta + '/');
  }

  navegarA(ruta: string): void {
    this.closeSidebar();
    this.router.navigate([ruta]);
  }

  logout() { this.ngZone.run(() => this.auth.logout()); }
  closeSidebar() { this.sidebarOpen = false; }

  ngOnDestroy() {
    this.subs.forEach(s => s.unsubscribe());
  }
}
