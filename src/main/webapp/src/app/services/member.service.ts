import { Injectable } from '@angular/core';
import {HttpClient, HttpResponse} from "@angular/common/http";
import {SERVER_API_URL} from "../app.constants";
import {Observable} from "rxjs";
import {Member} from "../models/member";
import {MemberSkill} from "../models/member-skill";
import {MemberInfo} from "../models/member-info";

@Injectable({
  providedIn: 'root'
})
export class MemberService {

  url = SERVER_API_URL + '/member';

  constructor(private httpClient: HttpClient) { }

  getAll(): Observable<MemberInfo[]> {
    return this.httpClient.get<MemberInfo[]>(this.url + '/list');
  }

  getMember(memberId: number): Observable<Member> {
    return this.httpClient.get<Member>(this.url + `/${memberId}`);
  }

  getMemberSkills(memberId: number): Observable<MemberSkill> {
    return this.httpClient.get<MemberSkill>(this.url + `/${memberId}/skills`);
  }

  saveMember(member: Member): Observable<HttpResponse<any>> {
    return this.httpClient.post(this.url, member, {observe: 'response'});
  }

  updateMemberSkills(memberId: number, memberSkill: MemberSkill): Observable<HttpResponse<any>> {
    return this.httpClient.put(this.url + `/${memberId}/skills`, memberSkill, {observe: 'response'});
  }

  deleteMemberSkill(memberId: number, skillName: string): Observable<any> {
    return this.httpClient.delete(this.url + `/${memberId}?skillName=${skillName}`);
  }

}
