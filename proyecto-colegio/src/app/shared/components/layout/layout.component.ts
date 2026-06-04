import { Component, Input, OnInit, NgZone } from '@angular/core';
import { CommonModule } from '@angular/common';
import { Router } from '@angular/router';
import { IonIcon } from '@ionic/angular/standalone';
import { addIcons } from 'ionicons';
import {
  gridOutline, peopleOutline, schoolOutline, bookOutline, clipboardOutline,
  documentTextOutline, starOutline, checkmarkCircleOutline, pencilOutline,
  chatbubblesOutline, personOutline, menuOutline, logOutOutline
} from 'ionicons/icons';
import { AuthService, AuthUser } from '../../../core/services/auth.service';

@Component({
  selector: 'app-layout',
  standalone: true,
  imports: [CommonModule, IonIcon],
  templateUrl: './layout.component.html',
  styleUrls: ['./layout.component.scss']
})
export class LayoutComponent implements OnInit {
  @Input() pageTitle = 'Dashboard';
  currentUser: AuthUser | null = null;
  sidebarOpen = false;

  constructor(
    private auth: AuthService,
    private router: Router,
    private ngZone: NgZone
  ) {
    addIcons({
      gridOutline, peopleOutline, schoolOutline, bookOutline, clipboardOutline,
      documentTextOutline, starOutline, checkmarkCircleOutline, pencilOutline,
      chatbubblesOutline, personOutline, menuOutline, logOutOutline
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

  ngOnInit() { this.currentUser = this.auth.currentUser; }

  isActive(ruta: string): boolean {
    return this.router.url === ruta || this.router.url.startsWith(ruta + '/');
  }

  navegarA(ruta: string): void {
    this.closeSidebar();
    this.router.navigate([ruta]);
  }

  logout() { this.ngZone.run(() => this.auth.logout()); }
  closeSidebar() { this.sidebarOpen = false; }
}
