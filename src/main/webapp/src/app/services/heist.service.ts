import {Injectable} from '@angular/core';
import {SERVER_API_URL} from '../app.constants';
import {HttpClient, HttpResponse} from '@angular/common/http';
import {Observable} from 'rxjs';
import {Heist} from '../models/heist';
import {HeistSkill} from '../models/heist-skill';
import {HeistMember} from '../models/heist-member';
import {MembersEligibleForHeist} from '../models/members-eligible-for-heist';
import {HeistInfo} from '../models/heist-info';
import {HeistMembers} from '../models/heist-members';
import {HeistOutcome} from '../models/heist-outcome';
import {HeistStatus} from '../models/heist-status';
import {HeistSkills} from '../models/heist-skills';

@Injectable({
  providedIn: 'root'
})
export class HeistService {

  url = SERVER_API_URL + '/heist';

  constructor(private httpClient: HttpClient) { }

  getHeist(heistId: number): Observable<Heist> {
    return this.httpClient.get<Heist>(this.url + `/${heistId}`);
  }

  getAll(): Observable<HeistInfo[]> {
    return this.httpClient.get<HeistInfo[]>(this.url + '/list');
  }

  getHeistSkills(heistId: number): Observable<HeistSkill> {
    return this.httpClient.get<HeistSkill>(this.url + `/${heistId}/skills`);
  }

  getMembersEligibleForHeist(heistId: number): Observable<MembersEligibleForHeist> {
    return this.httpClient.get<MembersEligibleForHeist>(this.url + `/${heistId}/eligible_members`);
  }

  getHeistMembers(heistId: number): Observable<HeistMember[]> {
    return this.httpClient.get<HeistMember[]>(this.url + `/${heistId}/members`);
  }

  getHeistStatus(heistId: number): Observable<HeistStatus> {
    return this.httpClient.get<HeistStatus>(this.url + `/${heistId}/status`);
  }

  getHeistOutcome(heistId: number): Observable<HeistOutcome> {
    return this.httpClient.get<HeistOutcome>(this.url + `/${heistId}/outcome`);
  }

  saveHeist(heist: Heist): Observable<HttpResponse<any>> {
    return this.httpClient.post(this.url, heist, {observe: 'response'});
  }

  updateHeistSkills(heistId: number, skills: HeistSkills): Observable<HttpResponse<any>> {
    return this.httpClient.patch<HeistSkills>(this.url + `/${heistId}/skills`, skills, {observe: 'response'});
  }

  saveHeistMembers(heistId: number, members: HeistMembers): Observable<HttpResponse<any>> {
    return this.httpClient.put<HttpResponse<any>>(this.url + `/${heistId}/members`, members, {observe: 'response'});
  }

  startHeist(heistId: number): Observable<HttpResponse<any>> {
    return this.httpClient.put<HttpResponse<any>>(this.url + `/${heistId}/start`, {observe: 'response'});
  }

}
