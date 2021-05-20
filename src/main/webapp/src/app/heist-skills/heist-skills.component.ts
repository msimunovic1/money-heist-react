import {Component, EventEmitter, Input, OnInit, Output} from '@angular/core';
import {HeistSkill} from "../models/heist-skill";

@Component({
  selector: 'app-heist-skills',
  templateUrl: './heist-skills.component.html',
  styleUrls: ['./heist-skills.component.css']
})
export class HeistSkillsComponent implements OnInit {

  numberList: any[] = [];

  /*ng2-smart-table settings*/
  settings = {
    actions: {
      delete: false
    },
    add: {
      confirmCreate: true
    },
    edit: {
      confirmSave: true,
    },
    columns: {
      name: {
        title: 'SKILL NAME',
      },
      level: {
        title: 'LEVEL',
      },
      members: {
        title: 'REQUIRED MEMBERS',
        editor: {
          type: 'list',
          config: {
            list: this.numberList
          }
        }
      },
    },
  };

  @Input()
  skills: HeistSkill[] = [];

  @Output()
  addedSkill: EventEmitter<HeistSkill> = new EventEmitter<HeistSkill>();

  @Output()
  update: EventEmitter<HeistSkill> = new EventEmitter<HeistSkill>();

  @Output()
  updatedSkill: EventEmitter<HeistSkill> = new EventEmitter<HeistSkill>();

  constructor() { }

  ngOnInit(): void {
    for (let i=0; i<50; i++) {
      this.numberList.push({value:i, title:i});
    }
  }

  onCreateConfirm(event: any) {
    this.addedSkill.emit(event.newData);
  }

  onUpdateConfirm(event: any) {
    this.updatedSkill.emit(event.newData);
    this.update.emit(event.confirm.resolve());
  }

}
