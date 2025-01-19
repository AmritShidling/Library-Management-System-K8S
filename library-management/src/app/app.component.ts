import { Component, ViewChild } from '@angular/core';
import { BooksService } from './services/books.service';
import { UsersService } from './services/users.service';
import { BorrowingService } from './services/borrowing.service';
import { BorrowingHistoryService } from './services/borrowing-history.service';
import { BooksComponent } from './books/books.component';
import { BorrowHistoryComponent } from './borrowing-history/borrowing-history.component';
import { BorrowingComponent } from './borrowing/borrowing.component';
import { UsersComponent } from './users/users.component';

@Component({
  selector: 'app-root',
  templateUrl: './app.component.html',
  styleUrls: ['./app.component.css']
})
export class AppComponent {
  title = 'library-management';

  @ViewChild(BooksComponent) booksComponent!: BooksComponent;
  @ViewChild(BorrowHistoryComponent) borrowHistoryComponent!: BorrowHistoryComponent;
  @ViewChild(BorrowingComponent) BorrowingComponent!: BorrowingComponent;
  @ViewChild(UsersComponent) usersComponent!: UsersComponent;
  onTabChange(event: any): void {
    switch (event.index) {
      case 3: 
        this.borrowHistoryComponent.fetchAllHistory();
        break;
      case 2: 
        
        this.BorrowingComponent.loadBooks();
        this.BorrowingComponent.loadUsers();
        break;
      case 2: 
        this.usersComponent.loadUsers();
        break;
      case 1: 
        this.booksComponent.loadBooks();
        break;
    }
  }
}
