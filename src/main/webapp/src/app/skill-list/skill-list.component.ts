import {Component, EventEmitter, Input, OnInit, Output} from '@angular/core';

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

  @Output()
  addedSkill: EventEmitter<any> = new EventEmitter<any>();

  @Output()
  updatedSkill: EventEmitter<any> = new EventEmitter<any>();

  /*ng2-smart-table settings*/
  settings: any;

  constructor() { }

  ngOnInit(): void {
    this.settings = {
      delete: {
        confirmDelete: true
      },
      add: {
        confirmCreate: true
      },
      edit: {
        confirmSave: true,
      },
      columns:
      this.columns,
    };
  }

  onCreateConfirm(event: any) {
    this.addedSkill.emit(event.newData);
  }

  onUpdateConfirm(event: any) {
    this.updatedSkill.emit(event.confirm.resolve());
  }

}
