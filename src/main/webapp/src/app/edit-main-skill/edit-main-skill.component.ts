import {Component, Input, OnInit} from '@angular/core';
import {MemberDetailsComponent} from "../member-details/member-details.component";
import {Skill} from "../models/skill";
import {NbDialogRef} from "@nebular/theme";

@Component({
  selector: 'app-edit-main-skill',
  templateUrl: './edit-main-skill.component.html',
  styleUrls: ['./edit-main-skill.component.css'],
})
export class EditMainSkillComponent implements OnInit {

  @Input() memberSkills: Skill[] = [];
  @Input() mainSkill: string = '';
  @Input() memberId: number = 0;

  constructor(private memberDetailsComponent: MemberDetailsComponent,
              protected ref: NbDialogRef<EditMainSkillComponent>) { }

  ngOnInit(): void {
  }

  editMainSkill() {
    this.memberDetailsComponent.saveUpdatedSkills();
    this.ref.close();
  }

  close() {
    this.ref.close();
  }
}
