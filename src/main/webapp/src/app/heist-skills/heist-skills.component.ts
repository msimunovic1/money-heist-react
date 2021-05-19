import {Component, EventEmitter, Input, OnInit, Output} from '@angular/core';
import {HeistSkill} from "../models/heist-skill";

@Component({
  selector: 'app-heist-skills',
  templateUrl: './heist-skills.component.html',
  styleUrls: ['./heist-skills.component.css']
})
export class HeistSkillsComponent implements OnInit {

  numberList: any[] = [];

  settings = {
    delete: {
      confirmDelete: true
    },
    add: {
      confirmCreate: true
    },
    edit: {
      confirmSave: true,
    },
    columns: {
      name: {
        title: 'Name',
      },
      level: {
        title: 'Level',
      },
      members: {
        title: 'Required Members',
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
  heistSkills: HeistSkill[] = [];

  @Output()
  addedSkill: EventEmitter<HeistSkill> = new EventEmitter<HeistSkill>();

  @Output()
  updatedSkill: EventEmitter<HeistSkill> = new EventEmitter<HeistSkill>();


  constructor() { }

  ngOnInit(): void {
    for (let i=0; i<50; i++) {
      this.numberList.push({value:i ,title:i});
    }
  }

  onCreateConfirm(event: any) {
    this.addedSkill.emit(event.newData);
  }

  onUpdateConfirm(event: any) {
    this.updatedSkill.emit(event.confirm.resolve());
  }

}
