import { Component, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { IonContent, IonIcon } from '@ionic/angular/standalone';
import { LayoutComponent } from '../../../shared/components/layout/layout.component';
import { addIcons } from 'ionicons';
import { pencilOutline } from 'ionicons/icons';
import { AuthService } from '../../../core/services/auth.service';
import { ApiService } from '../../../core/services/api.service';

@Component({
  selector: 'app-alumno-anotaciones',
  standalone: true,
  imports: [CommonModule, IonContent, IonIcon, LayoutComponent],
  templateUrl: './alumno-anotaciones.page.html',
  styleUrls: ['./alumno-anotaciones.page.scss']
})
export class AlumnoAnotacionesPage implements OnInit {
  anotaciones: any[] = [];
  constructor(private auth: AuthService, private api: ApiService) { addIcons({ pencilOutline }); }

  ngOnInit() {
    const uid = this.auth.currentUser?.id ?? 0;
    if (!uid) return;
    this.api.getAnotacionesAlumno(uid).subscribe({ next: data => { this.anotaciones = data; } });
  }

  badge(t: string) { return { POSITIVA: 'badge-success', NEGATIVA: 'badge-danger', NEUTRA: 'badge-neutral' }[t] || 'badge-neutral'; }
}