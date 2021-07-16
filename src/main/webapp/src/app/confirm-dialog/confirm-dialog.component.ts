import {Component, Input, OnInit} from '@angular/core';
import {NbDialogRef} from '@nebular/theme';
import {DialogService} from '../services/dialog.service';
import {CONFIRMATION_CREATE_HEADER, CONFIRMATION_UPDATE_HEADER} from '../app.constants';

@Component({
  selector: 'app-confirm-dialog',
  templateUrl: './confirm-dialog.component.html',
  styleUrls: ['./confirm-dialog.component.css']
})
export class ConfirmDialogComponent implements OnInit {

  @Input()
  header = '';

  @Input()
  body = '';

  constructor(protected ref: NbDialogRef<ConfirmDialogComponent>,
              private dialogService: DialogService) { }

  ngOnInit(): void {
  }

  confirm() {
    if (this.header === CONFIRMATION_CREATE_HEADER) {
      this.dialogService.confirmCreate(true);
    } else if (this.header === CONFIRMATION_UPDATE_HEADER) {
      this.dialogService.confirmUpdate(true);
    }
    else {
      this.dialogService.confirmDelete(true);
    }
    this.close();
  }

  close() {
    this.ref.close();
  }

}
