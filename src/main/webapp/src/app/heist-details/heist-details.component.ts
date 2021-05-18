import { Component, OnInit } from '@angular/core';
import {HeistService} from "../services/heist.service";
import {Heist} from "../models/heist";
import {ActivatedRoute} from "@angular/router";
import {HeistMember} from "../models/heist-member";

@Component({
  selector: 'app-heist-details',
  templateUrl: './heist-details.component.html',
  styleUrls: ['./heist-details.component.css']
})
export class HeistDetailsComponent implements OnInit {

  heist: Heist = new Heist();
  heistId: number = 0;

  heistMembers: HeistMember[] = [];
  heistStatus: string = '';
  heistOutcome: string = '';

  constructor(private heistService: HeistService,
              private route: ActivatedRoute) { }

  ngOnInit(): void {
    this.route.paramMap.subscribe(() => {
      this.handleHeistDetails();
    });
  }

  handleHeistDetails() {
    // @ts-ignore
    this.heistId = +this.route.snapshot.paramMap.get('id');

    // get heist details from service
    this.heistService.getHeist(this.heistId).subscribe(
      data => this.heist = data
    );

    // get heist members from service
    this.heistService.getHeistMembers(this.heistId).subscribe(
      data => this.heistMembers = data
    );

    // get heist status from service
    this.heistService.getHeistStatus(this.heistId).subscribe(
      data => this.heistStatus = data
    );

    // get heist outcome from service
    this.heistService.getHeistOutcome(this.heistId).subscribe(
      data => this.heistOutcome = data
    );
  }
}
