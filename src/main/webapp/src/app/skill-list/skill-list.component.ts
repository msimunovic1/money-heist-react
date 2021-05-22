import {Component, EventEmitter, Input, OnChanges, OnInit, Output, SimpleChanges} from '@angular/core';

@Component({
  selector: 'app-skill-list',
  templateUrl: './skill-list.component.html',
  styleUrls: ['./skill-list.component.css']
})
export class SkillListComponent implements OnInit, OnChanges {

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

  constructor() {
  }

  ngOnChanges(changes: SimpleChanges): void {

        console.log("Changes", changes)
    }

  ngOnInit(): void {
    this.settings = {
      actions: {
        delete: false,
        edit: false
      },
      add: {
        confirmCreate: true
      },
      columns: this.columns
    };
  }

  onCreateConfirm(event: any) {
    this.addedSkill.emit(event.newData);
  }

  onUpdateConfirm(event: any) {
    this.updatedSkill.emit(event.confirm.resolve());
  }

}
