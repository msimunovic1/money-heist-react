import { Injectable } from '@angular/core';
import {Subject} from 'rxjs';

@Injectable({
  providedIn: 'root'
})
export class DialogService {

  private isDeleteConfirmed = new Subject<boolean>();
  private isCreateConfirmed = new Subject<boolean>();
  private isUpdateConfirmed = new Subject<boolean>();

  isDeleteConfirmed$ = this.isDeleteConfirmed.asObservable();
  isCreateConfirmed$ = this.isCreateConfirmed.asObservable();
  isUpdateConfirmed$ = this.isUpdateConfirmed.asObservable();

  confirmDelete(confirmation: boolean) {
    this.isDeleteConfirmed.next(confirmation);
  }

  confirmCreate(confirmation: boolean) {
    this.isCreateConfirmed.next(confirmation);
  }

  confirmUpdate(confirmation: boolean) {
    this.isUpdateConfirmed.next(confirmation);
  }

}
