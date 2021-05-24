import {Component, OnInit} from '@angular/core';
import {Member} from "../models/member";
import {MemberService} from "../services/member.service";
import {ActivatedRoute, Route, Router} from "@angular/router";
import {LocalDataSource} from "ng2-smart-table";
import {MemberSkill} from "../models/member-skill";
import {Skill} from "../models/skill";
import {NbToastrService} from "@nebular/theme";
import {HeistSkill} from "../models/heist-skill";

@Component({
  selector: 'app-member-details',
  templateUrl: './member-details.component.html',
  styleUrls: ['./member-details.component.css']
})
export class MemberDetailsComponent implements OnInit {

  member: Member = new Member();
  memberId: number = 0;
  sex: string = '';
  tagStatus: string = 'basic';

  source: LocalDataSource = new LocalDataSource();

  updatedMemberSkills: Skill[] = [];

  /*ng2-smart-table settings*/
  skillTableColumns = {
    name: {
      title: 'SKILL NAME',
    },
    level: {
      title: 'LEVEL',
    }
  }

  skillTableActions = {
    edit: false
  }

  constructor(private memberService: MemberService,
              private route: ActivatedRoute,
              private router: Router,
              private toastrService: NbToastrService) { }

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

  // add new skills to list
  addSkill(event: HeistSkill) {
    this.source.add(event).then(() => this.refreshUpdateSkillList())
  }

  // update/delete skills from list
  updateSkill(event: HeistSkill) {
    this.source.remove(event).then(() => this.refreshUpdateSkillList())
  }

  refreshUpdateSkillList() {
    this.source.getAll().then(data => {
      this.source.refresh();
      this.updatedMemberSkills = data;
    });
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

  deleteSkill(skillName: string) {
    this.memberService.deleteMemberSkill(this.memberId, skillName).subscribe(
      res => {
        if(res.status === 204) {
          this.toastrService.success('Skills deleted', 'Success');
        }
      }
    );
  }

  // save updated skills
  saveUpdatedSkills() {
    this.memberService.updateMemberSkills(this.memberId, new MemberSkill(this.updatedMemberSkills, "")).subscribe(
      res => {
        if(res.status === 204) {
          this.toastrService.success('Skills updated', 'Success')
        }
      }
    );
  }
}
