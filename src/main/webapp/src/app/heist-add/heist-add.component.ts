import {Component, OnInit} from '@angular/core';
import {FormArray, FormBuilder, FormControl, FormGroup, Validators} from "@angular/forms";
import {HeistService} from "../services/heist.service";
import {Heist} from "../models/heist";
import {HeistSkill} from "../models/heist-skill";
import {NbDateService, NbToastrService} from "@nebular/theme";
import {PRIMARY_OUTLET, Router, UrlSegment} from "@angular/router";
import {MemberService} from "../services/member.service";

@Component({
  selector: 'app-heist-add',
  templateUrl: './heist-add.component.html',
  styleUrls: ['./heist-add.component.css']
})
export class HeistAddComponent implements OnInit {

  min: Date;

  heistFormGroup: FormGroup = this.formBuilder.group({
    name: new FormControl('', [Validators.required]),
    location: new FormControl('', [Validators.required]),
    startTime: new FormControl('', [Validators.required]),
    endTime: new FormControl('', [Validators.required]),
    skills: this.formBuilder.array([])
  });

  constructor(private formBuilder: FormBuilder,
              private heistService: HeistService,
              private router: Router,
              public toastrService: NbToastrService,
              protected dateService: NbDateService<Date>) {
    // validation - endTime can't be picked in past
    this.min = this.dateService.addMonth(this.dateService.today(), 0);
  }

  ngOnInit(): void {
  }

  get skills() {
    return this.heistFormGroup.controls['skills'] as FormArray;
  }

  get startTime() {
    return this.heistFormGroup.get('startTime') as FormControl;
  }

  get endTime() {
    return this.heistFormGroup.get('endTime') as FormControl;
  }

  addSkill() {
    const skillForm = this.formBuilder.group({
      name: new FormControl('', [Validators.required]),
      level: new FormControl('', [Validators.required]),
      members: new FormControl('', [Validators.required])
    })
    this.skills.push(skillForm);
  }

  deleteSkill(skillIndex: number) {
    this.skills.removeAt(skillIndex);
  }

  onSubmit() {

    // Touching all fields triggers the display of the error messages
    // TODO: add error messages
    if(this.heistFormGroup.invalid) {
      this.toastrService.warning("Please fill in all required fields.", "Required fields")
      this.heistFormGroup.markAllAsTouched();
      return;
    }

    let heist = new Heist();
    heist.name = this.heistFormGroup.value.name;
    heist.location = this.heistFormGroup.value.location;
    heist.startTime = this.heistFormGroup.value.startTime;
    heist.endTime = this.heistFormGroup.value.endTime;

    let skills: HeistSkill[] = [];
    let heistSkills = this.heistFormGroup.value.skills;
    heistSkills.map((skill: HeistSkill) => skills.push(skill));

    heist.skills = skills;

    this.heistService.saveHeist(heist).subscribe(
      res => {
        this.resetForm();
        // get url segments from location response header
        const urlSegments: UrlSegment[]  = this.router.parseUrl(<string>res.headers.get('location')).root.children[PRIMARY_OUTLET].segments;
        // get user id from location response header segment
        const id = urlSegments[1].path;

        this.router.navigate(['/heist/', id]);
      }
    )

  }

  resetForm() {
    //reset the form
    this.heistFormGroup.reset();
  }


}
