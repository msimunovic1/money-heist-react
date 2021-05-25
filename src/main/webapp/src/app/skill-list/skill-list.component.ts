import {Component, EventEmitter, Input, OnInit, Output, TemplateRef} from '@angular/core';
import {NbDialogService} from "@nebular/theme";
import {UpdatedSkill} from "../models/updated-skill";

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

  /*ng2-smart-table settings*/
  settings: any;

  skillName: string = '';
  isConfirmed: boolean = false;

  constructor(private dialogService: NbDialogService) {
  }

  ngOnInit(): void {
    this.settings = {
      actions: this.actions,
      add: {
        confirmCreate: true
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
    this.addedSkill.emit(event.newData);
  }

  onUpdateConfirm(event: any) {
    this.updatedSkill.emit(new UpdatedSkill(event.data, event.newData));
  }

  onDeleteConfirm(event: any, dialog: TemplateRef<any>) {
      this.dialogService.open(dialog, {closeOnEsc: true})
        .onClose.subscribe(() => {
          if(this.isConfirmed) {
            this.deletedSkill.emit(event.data.name)
            this.isConfirmed = false;
          }
      });
  }

  confirm(ref: any) {
    this.isConfirmed = true;
    ref.close();
  }

}
