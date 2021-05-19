import { Component, OnInit } from '@angular/core';
import {HeistService} from "../services/heist.service";
import {HeistInfo} from "../models/heist-info";
import {MemberService} from "../services/member.service";
import {MemberInfo} from "../models/member-info";

@Component({
  selector: 'app-heist-list',
  templateUrl: './heist-list.component.html',
  styleUrls: ['./heist-list.component.css']
})
export class HeistListComponent implements OnInit {

  heists: HeistInfo[] = [];
  members: MemberInfo[] = [];

  constructor(private heistService: HeistService,
              private memberService: MemberService) { }

  ngOnInit(): void {

    this.heistService.getAll().subscribe(
      data => this.heists = data
    )

    this.memberService.getAll().subscribe(
      data => this.members = data
    )

  }

}
