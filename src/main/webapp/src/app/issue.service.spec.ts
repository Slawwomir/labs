import { TestBed } from '@angular/core/testing';

import { IssueService } from './issue-service.service';

describe('IssueServiceService', () => {
  beforeEach(() => TestBed.configureTestingModule({}));

  it('should be created', () => {
    const service: IssueService = TestBed.get(IssueService);
    expect(service).toBeTruthy();
  });
});
