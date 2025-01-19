import { Component, OnInit } from '@angular/core';
import { FormBuilder, FormGroup, Validators } from '@angular/forms';
import { BorrowingService } from '../services/borrowing.service';
import { MatSnackBar } from '@angular/material/snack-bar';

@Component({
  selector: 'app-borrowing',
  templateUrl: './borrowing.component.html',
  styleUrls: ['./borrowing.component.css']
})
export class BorrowingComponent implements OnInit {
  borrowForm!: FormGroup;
  users: any[] = []; // List of users
  books: any[] = []; // List of books
  errorMessage: string | null = null; // To display error messages
  successMessage: string | null = null; 
compareById: any;

  constructor(
    private fb: FormBuilder,
    private borrowingService: BorrowingService,
    private snackBar: MatSnackBar
  ) {}

  ngOnInit(): void {
    this.borrowForm = this.fb.group({
      userId: ['', Validators.required],
      bookId: ['', Validators.required],
    });

    this.loadUsers();
    this.loadBooks();
  }

  // Load all available users
  loadUsers(): void {
    this.borrowingService.getUsers().subscribe({
      next: (users) => (this.users = users),
      error: (err) => console.error('Failed to load users', err),
    });
  }

  // Load all available books
  loadBooks(): void {
    this.borrowingService.getAvailableBooks().subscribe({
      next: (books) => (this.books = books),
      error: (err) => console.error('Failed to load books', err),
    });
  }

  // Submit borrow request
  onBorrow(): void {
    if (this.borrowForm.valid) {
      const { userId, bookId } = this.borrowForm.value;

      this.borrowingService.borrowBook(userId, bookId).subscribe({
        next: () => {
          this.snackBar.open('Book borrowed successfully!', 'Close', {
            duration: 3000,
            panelClass: ['success-snackbar']
          });

          // Clear form fields
          this.borrowForm.reset();
          this.successMessage = 'Book is borrowed successfully';
          this.errorMessage = null; // Clear any previous error
          this.loadBooks(); // Refresh book list after borrowing
        },
        error: (err) => {
          if (err.error && err.error.message) {
            this.errorMessage = 'Book is not available';//err.error.message; // Display error message from backend
          } else {
            this.errorMessage = 'An error occurred while borrowing the book.';
          }
        },
      });
    }
  }
}
