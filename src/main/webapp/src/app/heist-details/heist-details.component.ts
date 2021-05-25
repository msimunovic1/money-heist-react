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
import {NbToastrService} from "@nebular/theme";
import {UpdatedSkill} from "../models/updated-skill";

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

  skillTableNumberList: any[] = [];

  skillTableColumns = {
    name: {
      title: 'SKILL NAME',
      editable: false
    },
    level: {
      title: 'LEVEL',
      editable: false
    },
    members: {
      title: 'REQUIRED MEMBERS',
      editor: {
        type: 'list',
        config: {
          list: this.skillTableNumberList
        }
      }
    },
  }

  skillTableActions = {
    delete: false,
  }

  constructor(private heistService: HeistService,
              private route: ActivatedRoute,
              private router: Router,
              private toastrService: NbToastrService) { }

  ngOnInit(): void {
    // get id from url param
    this.route.paramMap.subscribe(() => {
      this.handleHeistDetails();
    });

    for (let i=0; i<50; i++) {
      this.skillTableNumberList.push(
          { value:i, title:i }
        );
    }
  }

  disableUpdateSkillsBtn(){
    return this.updatedHeistSkills.length < 1;

  }

  // add new skills to list
  addSkill(event: HeistSkill) {
    this.source.add(event).then(() => this.refreshUpdateSkillList())
  }

  // update/delete skills from list
  updateSkill(event: UpdatedSkill) {
    this.source.update(event.oldData, event.newData).then(() => this.refreshUpdateSkillList())
  }

  refreshUpdateSkillList() {
    this.source.getAll().then(data => {
      this.source.refresh();
      this.updatedHeistSkills = data;
    });
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
      res => {
        if(res.status === 204) {
          this.toastrService.success('Skills updated', 'Success')
        }
      }
    );
  }

  // start heist manually
  startHeistManually(heistId: number) {
    this.heistService.startHeist(heistId).subscribe(
      () => this.ngOnInit()
    );
  }
}
