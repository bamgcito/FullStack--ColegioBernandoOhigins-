import { Component, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { IonContent, IonIcon } from '@ionic/angular/standalone';
import { LayoutComponent } from '../../../shared/components/layout/layout.component';
import { addIcons } from 'ionicons';
import { addOutline, closeOutline } from 'ionicons/icons';
import { ApiService } from '../../../core/services/api.service';

@Component({
  selector: 'app-admin-asignaturas',
  standalone: true,
  imports: [CommonModule, FormsModule, IonContent, IonIcon, LayoutComponent],
  templateUrl: './admin-asignaturas.page.html',
  styleUrls: ['./admin-asignaturas.page.scss']
})
export class AdminAsignaturasPage implements OnInit {
  asignaturas: any[] = [];
  showModal = false;
  loading = false;
  nuevoNombre = '';

  constructor(private api: ApiService) { addIcons({ addOutline, closeOutline }); }

  ngOnInit() { this.cargar(); }

  cargar() {
    this.loading = true;
    this.api.getAsignaturas().subscribe({
      next: data => { this.asignaturas = data; this.loading = false; },
      error: () => { this.loading = false; }
    });
  }

  crear() {
    if (!this.nuevoNombre) return;
    this.api.crearAsignatura({ nombre: this.nuevoNombre }, 'text').subscribe({
      next: () => { this.showModal = false; this.nuevoNombre = ''; this.cargar(); },
      error: () => { }
    });
  }
}