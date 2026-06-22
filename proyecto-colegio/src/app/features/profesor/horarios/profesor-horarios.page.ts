import { Component, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { IonContent, IonIcon } from '@ionic/angular/standalone';
import { LayoutComponent } from '../../../shared/components/layout/layout.component';
import { addIcons } from 'ionicons';
import { calendarOutline } from 'ionicons/icons';
import { AuthService } from '../../../core/services/auth.service';
import { ApiService } from '../../../core/services/api.service';

@Component({
  selector: 'app-profesor-horarios',
  standalone: true,
  imports: [CommonModule, IonContent, IonIcon, LayoutComponent],
  templateUrl: './profesor-horarios.page.html',
  styleUrls: ['./profesor-horarios.page.scss']
})
export class ProfesorHorariosPage implements OnInit {
  horarios: any[] = [];
  loading = true;
  dias = ['LUNES', 'MARTES', 'MIERCOLES', 'JUEVES', 'VIERNES'];

  constructor(private auth: AuthService, private api: ApiService) {
    addIcons({ calendarOutline });
  }

  ngOnInit() {
    const uid = this.auth.currentUser?.id;
    if (!uid) { this.loading = false; return; }
    this.api.getHorariosPorProfesor(uid).subscribe({
      next: data => { this.horarios = this.ordenar(data); this.loading = false; },
      error: () => { this.loading = false; }
    });
  }

  ordenar(data: any[]): any[] {
    const ord = ['LUNES', 'MARTES', 'MIERCOLES', 'JUEVES', 'VIERNES'];
    return [...data].sort((a, b) => {
      const da = ord.indexOf(a.diaSemana), db = ord.indexOf(b.diaSemana);
      return da !== db ? da - db : a.bloque - b.bloque;
    });
  }

  porDia(dia: string) { return this.horarios.filter(h => h.diaSemana === dia); }
}
