import {Component, OnInit, TemplateRef} from '@angular/core';
import {Member} from "../models/member";
import {MemberService} from "../services/member.service";
import {ActivatedRoute, Router} from "@angular/router";
import {LocalDataSource} from "ng2-smart-table";
import {Skill} from "../models/skill";
import {NbDialogService, NbToastrService} from "@nebular/theme";
import {MemberSkills} from "../models/member-skills";
import {NgForm} from "@angular/forms";
import {UpdatedSkill} from "../models/updated-skill";


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
  mainSkill: string = '';

  source: LocalDataSource = new LocalDataSource();

  memberSkills: Skill[] = [];

  /*ng2-smart-table settings*/
  skillTableColumns = {
    name: {
      title: 'SKILL NAME',
      editable: false
    },
    level: {
      title: 'LEVEL',
    }
  }

  constructor(private memberService: MemberService,
              private route: ActivatedRoute,
              private router: Router,
              private toastrService: NbToastrService,
              private dialogService: NbDialogService) { }

  ngOnInit(): void {
    // get id from url param
    this.route.paramMap.subscribe(() => {
      this.handleMemberDetails();
    });
  }

  editMainSkillDialog(dialog: TemplateRef<any>) {
    this.dialogService.open(dialog, {closeOnEsc: true});
/*    this.dialogService.open(
      EditMainSkillComponent, {
        closeOnEsc: true,
        context: {
          mainSkill: this.mainSkill,
          memberSkills: this.memberSkills,
          memberId: this.memberId
        }
    });*/
  }

  editMainSkill(mainSkillForm: NgForm, ref: any) {
    this.saveUpdatedSkills();
    ref.close();
  }

  handleMemberDetails() {
    this.memberId = +this.route.snapshot.params.id;

    // get member details from service
    this.memberService.getMember(this.memberId).subscribe(
      data => this.member = data,
      () => {},
      () => {

        if (this.member.skills) {
          this.memberSkills = this.member.skills;
        } else {
          this.memberSkills = [];
        }

        if (this.member.mainSkill) {
          this.mainSkill = this.member.mainSkill;
        } else {
          this.mainSkill = '';
        }

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

  // add new skills to list
  addSkill(event: MemberSkills) {
    this.source.add(event).then(() => {
      this.saveUpdatedSkills();
    })
  }

  // update skills from list
  updateSkill(event: UpdatedSkill) {
    this.source.update(event.oldData, event.newData).then(() => {
      this.saveUpdatedSkills();
    });
  }

  // save updated skills
  saveUpdatedSkills() {
    this.memberService.updateMemberSkills(this.memberId, new MemberSkills(this.memberSkills, this.mainSkill)).subscribe(
      res => {
        if(res.status === 204) {
          this.toastrService.success('Skills updated', 'Success');
          this.ngOnInit();
        }
      }, () => {
        this.ngOnInit()
      }
    );
  }

  // delete skill
  deleteSkill(skillName: string) {
    this.memberService.deleteMemberSkill(this.memberId, skillName).subscribe(
      res => {
        if(res.status === 204) {
          this.toastrService.success('Skills deleted', 'Success');
          this.ngOnInit()
        }
      }, () => {
        this.ngOnInit()
      }
    );
  }
}

