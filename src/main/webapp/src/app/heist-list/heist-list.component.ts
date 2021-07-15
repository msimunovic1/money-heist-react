import {Component, OnInit} from '@angular/core';
import {HeistService} from "../services/heist.service";
import {HeistInfo} from "../models/heist-info";

@Component({
  selector: 'app-heist-list',
  templateUrl: './heist-list.component.html',
  styleUrls: ['./heist-list.component.css']
})
export class HeistListComponent implements OnInit {

  heists: HeistInfo[] = [];

  constructor(private heistService: HeistService) { }

  ngOnInit(): void {

    this.heistService.getAll().subscribe(
      data => this.heists = data
    )

  }

}
