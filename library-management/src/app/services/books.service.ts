import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';
import { Book } from '../books/books.model';

@Injectable({
  providedIn: 'root'
})
export class BooksService {
  private apiUrl = 'http://book-service/v1/books';

  constructor(private http: HttpClient) {}

  getBooks(): Observable<Book[]> {
    return this.http.get<Book[]>(this.apiUrl);
  }

  getBook(id: number): Observable<Book> {
    return this.http.get<Book>(`${this.apiUrl}/${id}`);
  }

  addBook(book: Book): Observable<Book> {
    return this.http.post<Book>(this.apiUrl, book);
  }

  updateBook(id: number, book: Book): Observable<Book> {
    return this.http.put<Book>(`${this.apiUrl}/${id}`, book);
  }

  deleteBook(id: number): Observable<void> {
    return this.http.delete<void>(`${this.apiUrl}/${id}`);
  }

  updateAvailability(id: number, available: boolean): Observable<Book> {
    return this.http.put<Book>(`${this.apiUrl}/${id}/availability?available=${available}`, {});
  }

  searchBooks(query: string): Observable<Book[]> {
    return this.http.get<Book[]>(`${this.apiUrl}/search?query=${query}`);
  }
  getAvailableBooks(): Observable<any[]> {
    return this.http.get<any[]>(`${this.apiUrl}?available=true`,{});
  }
}
