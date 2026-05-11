import { Component, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { IonContent, IonIcon } from '@ionic/angular/standalone';
import { LayoutComponent } from '../../../shared/components/layout/layout.component';
import { addIcons } from 'ionicons';
import { addOutline, closeOutline } from 'ionicons/icons';
import { ApiService } from '../../../core/services/api.service';

@Component({
  selector: 'app-admin-cursos',
  standalone: true,
  imports: [CommonModule, FormsModule, IonContent, IonIcon, LayoutComponent],
  templateUrl: './admin-cursos.page.html',
  styleUrls: ['./admin-cursos.page.scss'],
  host: { class: 'ion-page' }
})
export class AdminCursosPage implements OnInit {
  cursos: any[] = [];
  profesores: any[] = [];
  showModal = false;
  loading = false;
  nuevo = { nivel: '', letra: '', anio: 2025, profesorJefeId: null as number | null };

  constructor(private api: ApiService) { addIcons({ addOutline, closeOutline }); }
  ngOnInit() { this.cargar(); }

  cargar() {
    this.api.getCursos().subscribe({ next: data => { this.cursos = data; } });
    this.api.getUsuarios().subscribe({
      next: data => { this.profesores = data.filter((u: any) => (u.nombreRol || u.rol) === 'PROFESOR'); }
    });
  }

  getProfesor(id: number | null) {
    if (!id) return 'Sin asignar';
    const p = this.profesores.find(p => p.id === id);
    return p ? `${p.nombre} ${p.apellido}` : '—';
  }

  crear() {
    if (!this.nuevo.nivel || !this.nuevo.letra) return;
    this.api.crearCurso(this.nuevo).subscribe({
      next: () => { this.showModal = false; this.nuevo = { nivel: '', letra: '', anio: 2025, profesorJefeId: null }; this.cargar(); }
    });
  }
}