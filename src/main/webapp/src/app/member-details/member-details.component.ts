import {Component, OnInit} from '@angular/core';
import {Member} from "../models/member";
import {MemberService} from "../services/member.service";
import {ActivatedRoute} from "@angular/router";
import {LocalDataSource} from "ng2-smart-table";
import {MemberSkill} from "../models/member-skill";
import {Skill} from "../models/skill";

@Component({
  selector: 'app-member-details',
  templateUrl: './member-details.component.html',
  styleUrls: ['./member-details.component.css', '../heist-skills/heist-skills.component.css']
})
export class MemberDetailsComponent implements OnInit {

  /*ng2-smart-table settings*/
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
        title: 'SKILL NAME'
      },
      level: {
        title: 'LEVEL'
      }
    },
  };

  source: LocalDataSource = new LocalDataSource();

  updatedMemberSkills: Skill[] = [];

  member: Member = new Member();
  memberId: number = 0;
  sex: string = '';
  tagStatus: string = 'basic';

  constructor(private memberService: MemberService,
              private route: ActivatedRoute) { }

  ngOnInit(): void {
    // get id from url param
    this.route.paramMap.subscribe(() => {
      this.handleMemberDetails();
    });
  }

  disableUpdateSkillsBtn(){
    if (this.updatedMemberSkills.length < 1) {
      return true;
    }
    return false;
  }

  onCreateConfirm(event: any) {

  }

  onUpdateConfirm(event: any) {

  }

  handleMemberDetails() {
    this.memberId = +this.route.snapshot.params.id;

    // get member details from service
    this.memberService.getMember(this.memberId).subscribe(
      data => this.member = data,
      () => {},
      () => {

        // add member skills to LocalDataSource
        this.source = new LocalDataSource(this.member.skills);

        if(this.member.sex === 'M') {
          this.sex = 'male'
        } else {
          this.sex = 'female'
        }
        switch (this.member.status) {
          case 'AVAILABLE':
            this.tagStatus = 'success';
            break;
          case 'EXPIRED':
            this.tagStatus = 'danger';
            break;
          case 'INCARCERATED':
            this.tagStatus = 'warning';
            break;
          case 'RETIRED':
            this.tagStatus = 'info';
            break;
        }
      }
    );
  }

  // save updated skills
  saveUpdatedSkills() {
    this.memberService.updateMemberSkills(this.memberId, new MemberSkill(this.updatedMemberSkills, "")).subscribe(
      res => console.log(res)
    );
  }
}
