import {Component, OnInit} from '@angular/core';
import {MemberService} from "../services/member.service";
import {FormArray, FormBuilder, FormControl, FormGroup, Validators} from "@angular/forms";
import {Member} from "../models/member";
import {Skill} from "../models/skill";
import {NbToastrService} from "@nebular/theme";
import {PRIMARY_OUTLET, Router, UrlSegment} from "@angular/router";

@Component({
  selector: 'app-member-add',
  templateUrl: './member-add.component.html',
  styleUrls: ['./member-add.component.css']
})
export class MemberAddComponent implements OnInit {

  mainSkill: string = '';
  mainSkillChecked = false;

  // radio button values
  genders = [
    { value: 'M', label: 'Male', checked: true },
    { value: 'F', label: 'Female' },
  ];

  statuses = [
    { value: 'AVAILABLE' },
    { value: 'EXPIRED' },
    { value: 'INCARCERATED' },
    { value: 'RETIRED' }
  ]

  memberFormGroup: FormGroup = this.formBuilder.group({
    name: new FormControl('', [Validators.required]),
    sex: new FormControl('', [Validators.required]),
    email: new FormControl('', [Validators.required, Validators.pattern("^[a-z0-9._%+-]+@[a-z0-9.-]+\\.[a-z]{2,4}$")]),
    status: new FormControl('', [Validators.required]),
    skills: this.formBuilder.array([])
  });

  constructor(private formBuilder: FormBuilder,
              private memberService: MemberService,
              private router: Router,
              public toastrService: NbToastrService) { }

  ngOnInit(): void {
  }

  get skills() {
    return this.memberFormGroup.controls['skills'] as FormArray;
  }

  addSkill() {
    const skillForm = this.formBuilder.group({
      name: new FormControl('', [Validators.required]),
      level: new FormControl(''),
      checked: false
    })
    this.skills.push(skillForm);
  }

  deleteSkill(skillIndex: number) {
    this.skills.removeAt(skillIndex);
  }

  checkMainSkill(checked: boolean, index: number) {

    this.mainSkill = this.skills.at(index).value.name;
    this.mainSkillChecked = checked;
  }

  onSubmit() {

    if(this.memberFormGroup.invalid) {
      this.toastrService.warning("Please fill in all required fields.", "Required fields")
      this.memberFormGroup.markAllAsTouched();
      return;
    }

    let member = new Member();
    member.name = this.memberFormGroup.value.name;
    member.sex = this.memberFormGroup.value.sex;
    member.email = this.memberFormGroup.value.email;
    member.status = this.memberFormGroup.value.status;

    let skills: Skill[] = [];
    let memberSkills = this.memberFormGroup.value.skills;
    memberSkills.map((skill: Skill) => skills.push(skill));

    member.skills = skills;

    this.memberService.saveMember(member).subscribe(
      res => {
        this.resetForm();

        this.resetForm();
        // get url segments from location response header
        const urlSegments: UrlSegment[]  = this.router.parseUrl(<string>res.headers.get('location')).root.children[PRIMARY_OUTLET].segments;
        // get user id from location response header segment
        const id = urlSegments[1].path;

        this.router.navigate(['/member/', id]);
      }
    )

  }

  resetForm() {
    //reset the form
    this.memberFormGroup.reset();
  }

}
