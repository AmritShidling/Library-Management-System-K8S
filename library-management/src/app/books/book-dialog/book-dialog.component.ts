import { Component, Inject, OnInit } from '@angular/core';
import { MatDialogRef, MAT_DIALOG_DATA } from '@angular/material/dialog';
import { FormBuilder, FormGroup, Validators } from '@angular/forms';
import { BooksService } from 'src/app/services/books.service';
import { Book } from '../books.model';  // Import the Book model

@Component({
  selector: 'app-book-dialog',
  templateUrl: './book-dialog.component.html',
  styleUrls: ['./book-dialog.component.css']
})
export class BookDialogComponent implements OnInit {
  bookForm!: FormGroup;
  isEdit: boolean = false;

  constructor(
    private dialogRef: MatDialogRef<BookDialogComponent>,
    @Inject(MAT_DIALOG_DATA) public data: any,
    private fb: FormBuilder,
    private booksService: BooksService
  ) {}

  ngOnInit(): void {
    // Check if we are editing an existing book
    this.isEdit = this.data ? true : false;

    // Initialize the form with either empty or pre-filled data
    this.initializeForm();
  }

  // Initialize the form with existing data if editing, or empty for a new book
  initializeForm(): void {
    this.bookForm = this.fb.group({
      title: [this.data?.title || '', Validators.required],
      author: [this.data?.author || '', Validators.required],
      isbn: [this.data?.isbn || '', Validators.required],
      available: [this.data?.available || true]  // Add availability field
    });
  }

  // Close the dialog
  onCancel(): void {
    this.dialogRef.close();
  }

  // Submit the form
  onSubmit(): void {
    if (this.bookForm.valid) {
      const bookData = this.bookForm.value;

      if (this.isEdit && this.data) {
        // Update book if editing
        this.booksService.updateBook(this.data.id, bookData).subscribe({
          next: (updatedBook) => {
            this.dialogRef.close(updatedBook);  // Close dialog and pass the updated book
          },
          error: (err) => {
            console.error('Error updating book:', err);
          }
        });
      } else {
        // Add new book
        this.booksService.addBook(bookData).subscribe({
          next: (book) => {
            this.dialogRef.close(book);  // Close dialog and pass the added book
          },
          error: (err) => {
            console.error('Error adding book:', err);
          }
        });
      }
    }
  }
}

// import { Component, Inject } from '@angular/core';
// import { MatDialogRef, MAT_DIALOG_DATA } from '@angular/material/dialog';
// import { FormBuilder, FormGroup, Validators } from '@angular/forms';
// import { BooksService } from 'src/app/services/books.service';


// @Component({
//   selector: 'app-book-dialog',
//   templateUrl: './book-dialog.component.html',
//   styleUrls: ['./book-dialog.component.css']
// })
// export class BookDialogComponent {
//   bookForm: FormGroup;

//   constructor(
//     private dialogRef: MatDialogRef<BookDialogComponent>,
//     @Inject(MAT_DIALOG_DATA) public data: any,
//     private fb: FormBuilder,
//     private booksService: BooksService
//   ) {
//     // Initialize form group with validation
//     this.bookForm = this.fb.group({
//       title: ['', Validators.required],
//       author: ['', Validators.required],
//       isbn: ['', [Validators.required, Validators.pattern('^[0-9]{13}$')]]
//     });
//   }

//   // Close the dialog
//   onCancel(): void {
//     this.dialogRef.close();
//   }

//   // Submit the form and add book
//   onSubmit(): void {
//     if (this.bookForm.valid) {
//       const bookData = this.bookForm.value;
//       this.booksService.addBook(bookData).subscribe({
//         next: (book) => {
//           this.dialogRef.close(book); // Close dialog and pass the added book
//         },
//         error: (err) => {
//           console.error('Error adding book:', err);
//         }
//       });
//     }
//   }
// }
