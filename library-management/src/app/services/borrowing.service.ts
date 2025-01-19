import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';

@Injectable({
  providedIn: 'root',
})
export class BorrowingService {
  private baseUrl = 'http://localhost:8083/v1/services'; 

  constructor(private http: HttpClient) {}

  // Get all users
  getUsers(): Observable<any[]> {
    return this.http.get<any[]>('http://localhost:8082/v1/users');
  }

  // Get all available books
  getAvailableBooks(): Observable<any[]> {
    return this.http.get<any[]>('http://localhost:8081/v1/books?available=true');
  }

  // Borrow a book
  borrowBook(userId: number, bookId: number): Observable<any> {
    return this.http.post<any>(`${this.baseUrl}/borrow`, null, {
      params: { userId: userId.toString(), bookId: bookId.toString() },
    });
  }
  
}
