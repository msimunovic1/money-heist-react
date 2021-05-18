import {Injectable} from '@angular/core';
import {SERVER_API_URL} from "../app.constants";
import {HttpClient, HttpResponse} from "@angular/common/http";
import {Observable} from "rxjs";
import {Heist} from "../models/heist";
import {HeistSkill} from "../models/heist-skill";
import {HeistMember} from "../models/heist-member";

@Injectable({
  providedIn: 'root'
})
export class HeistService {

  url = SERVER_API_URL + '/heist';

  constructor(private httpClient: HttpClient) { }

  getHeist(heistId: number): Observable<Heist> {
    return this.httpClient.get<Heist>(this.url + `/${heistId}`);
  }

  getHeistSkills(heistId: number): Observable<HeistSkill> {
    return this.httpClient.get<HeistSkill>(this.url + `/${heistId}/skills`);
  }

  getHeistMembers(heistId: number): Observable<HeistMember[]> {
    return this.httpClient.get<HeistMember[]>(this.url + `/${heistId}/members`);
  }

  getHeistStatus(heistId: number): Observable<string> {
    return this.httpClient.get<string>(this.url + `/${heistId}/status`);
  }

  getHeistOutcome(heistId: number): Observable<string> {
    return this.httpClient.get<string>(this.url + `/${heistId}/outcome`);
  }

  saveHeist(heist: Heist): Observable<HttpResponse<any>> {
    return this.httpClient.post(this.url, heist, {observe: 'response'});
  }

  updateHeistSkills(heistId: number, skills: HeistSkill[]): Observable<HttpResponse<any>> {
    return this.httpClient.patch(this.url + `/${heistId}`, skills, {observe: 'response'});
  }

}
