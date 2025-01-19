import { Injectable } from '@angular/core';
import { HttpClient, HttpParams } from '@angular/common/http';
import { Observable } from 'rxjs';
import { BorrowRecord } from '../borrowing-history/BorrowRecord';

@Injectable({
  providedIn: 'root'
})
export class BorrowingHistoryService {
  private baseUrl = 'http://localhost:8083/v1/services';

  constructor(private http: HttpClient) {}

  getBorrowHistory(): Observable<any[]> {
    return this.http.get<any[]>(`${this.baseUrl}/borrow-history`);
  }

  getBorrowHistoryByUser(userId: number): Observable<any[]> {
    return this.http.get<any[]>(`${this.baseUrl}/borrow-history/${userId}`);
  }

  getBorrowHistoryByBook(bookId: number): Observable<any[]> {
    return this.http.get<any[]>(`${this.baseUrl}/borrow-history/${bookId}`);
  }

  searchBorrowHistory(userName: string, bookTitle: string): Observable<BorrowRecord[]> {
    const params = new HttpParams()
      .set('userName', userName)
      .set('bookTitle', bookTitle);

    return this.http.get<BorrowRecord[]>(`${this.baseUrl}/borrow-history/search`, { params });
  }
  returnBook(borrowId: number): Observable<any> {
    return this.http.post(`${this.baseUrl}/return/${borrowId}`, {}); // You may adjust the payload if needed
  }

}
