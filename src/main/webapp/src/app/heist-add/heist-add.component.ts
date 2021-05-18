import {Component, OnInit} from '@angular/core';
import {FormArray, FormBuilder, FormControl, FormGroup, Validators} from "@angular/forms";
import {HeistService} from "../services/heist.service";
import {Heist} from "../models/heist";
import {HeistSkill} from "../models/heist-skill";
import {formatDate} from "@angular/common";
import {NbDateService} from "@nebular/theme";

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
    console.log(this.heistFormGroup.value)

    // Touching all fields triggers the display of the error messages
    // TODO: add error messages
    if(this.heistFormGroup.invalid) {
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
        console.log(res);
      }
    )

  }

  resetForm() {
    //reset the form
    this.heistFormGroup.reset();
  }


}
