import {Component, OnInit} from '@angular/core';
import {HeistService} from "../services/heist.service";
import {Heist} from "../models/heist";
import {ActivatedRoute, Router} from "@angular/router";
import {HeistMember} from "../models/heist-member";
import {HeistSkill} from "../models/heist-skill";
import {LocalDataSource} from "ng2-smart-table";
import {Skill} from "../models/skill";
import {HeistOutcome} from "../models/heist-outcome";
import {HeistStatus} from "../models/heist-status";

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

  source: LocalDataSource = new LocalDataSource();

  constructor(private heistService: HeistService,
              private route: ActivatedRoute,
              private router: Router) { }

  ngOnInit(): void {
    // get id from url param
    this.route.paramMap.subscribe(() => {
      this.handleHeistDetails();
    });

  }

  handleHeistDetails() {
    // TODO : provjeriti kako riješiti ovo bez @ts-ignore
    // @ts-ignore
    this.heistId = +this.route.snapshot.paramMap.get('id');

    // get heist details from service
    this.heistService.getHeist(this.heistId).subscribe(
      data => this.heist = data,
      () => {},
      () => {

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
            data => this.heistOutcome = data
          );
        }

      }
    );
    // get heist status from service
    this.heistService.getHeistStatus(this.heistId).subscribe(
      data => this.heistStatus = data
    );
  }

  // add new skills to list
  addSkill(event: HeistSkill) {
    this.source.add(event).then(() => {
      this.source.refresh();
      this.source.getAll().then(data =>{
        this.updatedHeistSkills = data;
      });
    })
  }

  // update/delete skills from list
  updateSkill(event: HeistSkill) {
    this.source.remove(event).then(() => this.source.refresh())
  }

  // save updated skills
  saveUpdatedSkills() {
    this.heistService.updateHeistSkills(this.heistId, this.updatedHeistSkills).subscribe(
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
