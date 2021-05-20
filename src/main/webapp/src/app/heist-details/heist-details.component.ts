import {Component, OnInit} from '@angular/core';
import {HeistService} from "../services/heist.service";
import {Heist} from "../models/heist";
import {ActivatedRoute, Router} from "@angular/router";
import {HeistMember} from "../models/heist-member";
import {HeistSkill} from "../models/heist-skill";
import {LocalDataSource} from "ng2-smart-table";
import {HeistOutcome} from "../models/heist-outcome";
import {HeistStatus} from "../models/heist-status";
import {HeistSkills} from "../models/heist-skills";

@Component({
  selector: 'app-heist-details',
  templateUrl: './heist-details.component.html',
  styleUrls: ['./heist-details.component.css']
})
export class HeistDetailsComponent implements OnInit {

  heist: Heist = new Heist();
  heistId: number = 0;

  updatedHeistSkills: HeistSkill[] = [];
  heistMembers: HeistMember[] = [];
  heistStatus: HeistStatus = new HeistStatus();
  heistOutcome: HeistOutcome = new HeistOutcome();

  tagStatus: string = 'basic';
  tagOutcome: string = 'basic';

  source: LocalDataSource = new LocalDataSource();

  constructor(private heistService: HeistService,
              private route: ActivatedRoute,
              private router: Router) { }

  ngOnInit(): void {
    this.route.paramMap.subscribe(() => {
      this.handleHeistDetails();
    });
  }

  disableUpdateSkillsBtn(){
    if (this.updatedHeistSkills.length < 1) {
      return true;
    }
    return false;
  }

  // add new skills to list
  addSkill(heistSkill: HeistSkill) {
    this.source.add(heistSkill).then(() => {
      // refresh skill list
      this.source.refresh();
      // add new skill to list
      this.addSkillToList(heistSkill)
    })
  }

  addSkillToList(heistSkill: HeistSkill) {
    this.updatedHeistSkills.push(heistSkill);
  console.log(this.updatedHeistSkills)
  }

  // update/delete skills from list
  updateSkill(event: HeistSkill) {
    this.source.remove(event).then(() => {
      this.source.refresh();
    })
  }

  handleHeistDetails() {

    // get id from url param
    this.heistId = +this.route.snapshot.params.id;

    // get heist details from service
    this.heistService.getHeist(this.heistId).subscribe(
      data => this.heist = data,
      () => {},
      () => {

        // add heist skills to LocalDataSource
        this.source = new LocalDataSource(this.heist.skills);

        if (this.heist.status !== 'PLANNING') {
          // get heist members from service
          this.heistService.getHeistMembers(this.heistId).subscribe(
            data => this.heistMembers = data
          );
        }

        if (this.heist.status === 'FINISHED') {
          // get heist outcome from service
          this.heistService.getHeistOutcome(this.heistId).subscribe(
            data => this.heistOutcome = data,
            () => {},
            () => this.tagOutcome = this.heistOutcome.outcome === 'SUCCEEDED' ? 'success' : 'basic'
          );
        }

        // set tag status to heist status
        switch (this.heist.status) {
          case 'PLANNING':
            this.tagStatus = 'info';
            break;
          case 'IN_PROGRESS':
            this.tagStatus = 'danger';
            break;
          case 'FINISHED':
            this.tagStatus = 'primary';
            break;
          case 'READY':
            this.tagStatus = 'warning';
            break;
        }
      }
    );
    // get heist status from service
    this.heistService.getHeistStatus(this.heistId).subscribe(
      data => this.heistStatus = data)
  }

  // save updated skills
  saveUpdatedSkills() {
    // convert skills members from string to number
    this.updatedHeistSkills.forEach(skill => {
      skill.members = +skill.members
    });

    this.heistService.updateHeistSkills(this.heistId, new HeistSkills(this.updatedHeistSkills)).subscribe(
      res => console.log(res)
    );
  }

  // start heist manually
  startHeistManually(heistId: number) {
    this.heistService.startHeist(heistId).subscribe(
      res => this.router.navigateByUrl(`/heist/${heistId}`)
    );
  }
}
