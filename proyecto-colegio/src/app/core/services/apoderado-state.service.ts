import { Injectable } from '@angular/core';
import { BehaviorSubject } from 'rxjs';

const STORAGE_KEY = 'apoderado_pupilo';

@Injectable({ providedIn: 'root' })
export class ApoderadoStateService {
  private pupiloSubject = new BehaviorSubject<any>(this.loadFromStorage());
  pupilo$ = this.pupiloSubject.asObservable();

  private loadFromStorage(): any {
    try {
      const stored = localStorage.getItem(STORAGE_KEY);
      return stored ? JSON.parse(stored) : null;
    } catch { return null; }
  }

  get pupilo() { return this.pupiloSubject.value; }
  set pupilo(p: any) {
    this.pupiloSubject.next(p);
    if (p) localStorage.setItem(STORAGE_KEY, JSON.stringify(p));
    else localStorage.removeItem(STORAGE_KEY);
  }

  clear() {
    this.pupiloSubject.next(null);
    localStorage.removeItem(STORAGE_KEY);
  }
}
