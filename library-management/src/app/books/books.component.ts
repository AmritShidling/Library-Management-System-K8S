import { Component, OnInit } from '@angular/core';
import { Book } from './books.model';  // Import your Book model
  import { BooksService } from '../services/books.service';
import { MatDialog } from '@angular/material/dialog';
import { BookDialogComponent } from './book-dialog/book-dialog.component';

@Component({
  selector: 'app-books',
  templateUrl: './books.component.html',
  styleUrls: ['./books.component.css']
})
export class BooksComponent implements OnInit {
  books: Book[] = [];
  displayedColumns: string[] = ['title', 'author', 'isbn', 'available', 'actions'];  // List columns here
  searchQuery: string = '';
  allBooks: Book[] = [];
  constructor(private booksService: BooksService, private dialog: MatDialog) { }

  ngOnInit(): void {
    this.booksService.getBooks().subscribe((data) => {
      this.allBooks = data;  // Store the original list of books
      this.books = [...this.allBooks];  // Display the full list initially
    });
  }

  loadBooks(): void {
    this.booksService.getBooks().subscribe((data: Book[]) => {
      this.books = data;
    });
  }

  editBook(book: Book): void {
    // Open edit dialog or navigate to the edit page
    console.log('Edit book', book);
  }

  searchBooks(event: Event): void {
    const inputElement = event.target as HTMLInputElement;
    const query = inputElement.value.toLowerCase();
  
    if (query) {
      this.booksService.searchBooks(query).subscribe((data) => {
        this.books = data; // Update the displayed books with search results
      });
    } else {
      this.books = [...this.allBooks];  // Reset to all books if the search query is empty
    }
  }
  deleteBook(bookId: number): void {
    this.booksService.deleteBook(bookId).subscribe(() => {
      this.loadBooks();  // Reload the books after deletion
    });
  }

  openAddBookDialog(): void {
    const dialogRef = this.dialog.open(BookDialogComponent);

    dialogRef.afterClosed().subscribe((result: Book) => {
      if (result) {
        this.loadBooks();
      }
    });
  }

  openEditBookDialog(book: Book): void {
    const dialogRef = this.dialog.open(BookDialogComponent, {
      data: book  // Pass the existing book data to the dialog for editing
    });
  
    dialogRef.afterClosed().subscribe((result: Book) => {
      if (result) {
        this.booksService.updateBook(book.id, result).subscribe(() => {
          this.loadBooks();  // Reload books after updating
        });
      }
    });
  }
}
