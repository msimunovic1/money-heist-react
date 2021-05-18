import { TestBed } from '@angular/core/testing';

import { HeistService } from './heist.service';

describe('HeistService', () => {
  let service: HeistService;

  beforeEach(() => {
    TestBed.configureTestingModule({});
    service = TestBed.inject(HeistService);
  });

  it('should be created', () => {
    expect(service).toBeTruthy();
  });
});
