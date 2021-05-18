import { Component, OnInit } from '@angular/core';
import {HeistService} from "../services/heist.service";
import {MembersEligibleForHeist} from "../models/members-eligible-for-heist";
import {ActivatedRoute} from "@angular/router";

@Component({
  selector: 'app-heist-eligible-members',
  templateUrl: './heist-eligible-members.component.html',
  styleUrls: ['./heist-eligible-members.component.css']
})
export class HeistEligibleMembersComponent implements OnInit {

  membersEligibleForHeist: MembersEligibleForHeist = new MembersEligibleForHeist();
  heistId: number = 0;

  heistMembers: string[] = [];

  constructor(private heistService: HeistService,
              private route: ActivatedRoute) { }

  ngOnInit(): void {
    // get id from url param
    this.route.paramMap.subscribe(() => {
      // @ts-ignore
      this.heistId = +this.route.snapshot.paramMap.get('id');

      this.heistService.getMembersEligibleForHeist(this.heistId).subscribe(
        data => this.membersEligibleForHeist = data
      );
    });

  }

  addMemberToHeist(name: string) {
    this.heistMembers.push(name);

    console.log(this.heistMembers)
  }
}
