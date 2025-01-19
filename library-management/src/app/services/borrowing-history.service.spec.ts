import { TestBed } from '@angular/core/testing';

import { BorrowingHistoryService } from './borrowing-history.service';

describe('BorrowingHistoryService', () => {
  let service: BorrowingHistoryService;

  beforeEach(() => {
    TestBed.configureTestingModule({});
    service = TestBed.inject(BorrowingHistoryService);
  });

  it('should be created', () => {
    expect(service).toBeTruthy();
  });
});
