import { TestBed } from '@angular/core/testing';

import { MemberService } from './member.service';
import {HttpClient} from '@angular/common/http';
import {MemberInfo} from '../models/member-info';
import {HttpClientTestingModule} from '@angular/common/http/testing';

describe('MemberService', () => {
  let service: MemberService;
  let http: HttpClient;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [
        HttpClientTestingModule
      ]
    });
    service = TestBed.inject(MemberService);
    http = TestBed.inject(HttpClient);
  });

  it('should be created', () => {
    expect(service).toBeTruthy();
  });

  it('should fetch all members', done => {
    const expected: MemberInfo[] = [
      {
        id: 1,
        name: 'Tokyo',
        status: 'AVAILABLE'
      }
    ];
    service.getAll().subscribe(data => {
      expect(http.get).toBeDefined();
      expect(http.get).toHaveBeenCalled();
      expect(data).toEqual(expected);
    });
    done();
  });
});
