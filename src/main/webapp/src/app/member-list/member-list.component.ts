import { Component, OnInit } from '@angular/core';
import {MemberService} from '../services/member.service';
import {MemberInfo} from '../models/member-info';

@Component({
  selector: 'app-member-list',
  templateUrl: './member-list.component.html',
  styleUrls: ['./member-list.component.css']
})
export class MemberListComponent implements OnInit {

  members: MemberInfo[] = [];

  constructor(private memberService: MemberService) { }

  ngOnInit(): void {

    this.memberService.getAll().subscribe(
      data => this.members = data
    );

  }

}
