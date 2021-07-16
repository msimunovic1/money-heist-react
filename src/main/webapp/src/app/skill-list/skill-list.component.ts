import {Component, EventEmitter, Input, OnInit, Output, TemplateRef, ViewChild} from '@angular/core';
import {NbDialogService} from '@nebular/theme';
import {UpdatedSkill} from '../models/updated-skill';
import {ConfirmDialogComponent} from '../confirm-dialog/confirm-dialog.component';
import {DialogService} from '../services/dialog.service';
import {
  CONFIRMATION_CREATE_BODY,
  CONFIRMATION_CREATE_HEADER,
  CONFIRMATION_DELETE_BODY,
  CONFIRMATION_DELETE_HEADER, CONFIRMATION_UPDATE_BODY, CONFIRMATION_UPDATE_HEADER
} from '../app.constants';

@Component({
  selector: 'app-skill-list',
  templateUrl: './skill-list.component.html',
  styleUrls: ['./skill-list.component.css']
})
export class SkillListComponent implements OnInit {

  @Input()
  skills: any[] = [];

  @Input()
  columns: any = {};

  @Input()
  actions: any = {};

  @Output()
  addedSkill: EventEmitter<any> = new EventEmitter<any>();

  @Output()
  updatedSkill: EventEmitter<UpdatedSkill> = new EventEmitter<UpdatedSkill>();

  @Output()
  deletedSkill: EventEmitter<any> = new EventEmitter<any>();

  @ViewChild(ConfirmDialogComponent)
  private confirmDialogComponent!: ConfirmDialogComponent;

  /*ng2-smart-table settings*/
  settings: any;

  skillName = '';

  // confirmation dialog flags
  isConfirmCreate = false;
  isConfirmUpdate = false;
  isConfirmDelete = false;

  constructor(private nbDialogService: NbDialogService,
              private dialogService: DialogService) {

    dialogService.isDeleteConfirmed$.subscribe(
      confirmation => this.isConfirmDelete = confirmation
    );

    dialogService.isCreateConfirmed$.subscribe(
      confirmation => this.isConfirmCreate = confirmation
    );

    dialogService.isUpdateConfirmed$.subscribe(
      confirmation => this.isConfirmUpdate = confirmation
    );

  }

  ngOnInit(): void {
    this.settings = {
      actions: this.actions,
      add: {
        confirmCreate: true,
      },
      delete: {
        confirmDelete : true,
      },
      edit: {
        confirmSave : true,
      },
      columns: this.columns
    };
  }

  onCreateConfirm(event: any) {
    this.nbDialogService.open(ConfirmDialogComponent,
      {
        context: {
          header: CONFIRMATION_CREATE_HEADER,
          body: CONFIRMATION_CREATE_BODY
        },
        closeOnEsc: true
      }).onClose.subscribe(() => {
        if (this.isConfirmCreate) {
          this.addedSkill.emit(event.newData);
          this.dialogService.confirmCreate(false);
        }
      });
  }

  onUpdateConfirm(event: any) {
    this.nbDialogService.open(ConfirmDialogComponent,
      {
        context: {
          header: CONFIRMATION_UPDATE_HEADER,
          body: CONFIRMATION_UPDATE_BODY
        },
        closeOnEsc: true
      }).onClose.subscribe(() => {
      if (this.isConfirmUpdate) {
        this.updatedSkill.emit(new UpdatedSkill(event.data, event.newData));
        this.dialogService.confirmUpdate(false);
      }
    });
  }

  onDeleteConfirm(event: any) {
      this.nbDialogService.open(ConfirmDialogComponent,
        {
          context: {
            header: CONFIRMATION_DELETE_HEADER,
            body: CONFIRMATION_DELETE_BODY
          },
          closeOnEsc: true
        }).onClose.subscribe(() => {
          if (this.isConfirmDelete) {
            this.deletedSkill.emit(event.data.name);
            this.dialogService.confirmDelete(false);
          }
      });
  }

}
