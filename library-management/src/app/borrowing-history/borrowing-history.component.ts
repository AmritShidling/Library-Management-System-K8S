import { Component, OnInit } from '@angular/core';
import { FormBuilder, FormGroup } from '@angular/forms';
import { BorrowingService } from 'src/app/services/borrowing.service';
import { BorrowingHistoryService } from '../services/borrowing-history.service';
import { BorrowRecord } from './BorrowRecord';

@Component({
  selector: 'app-borrow-history',
  templateUrl: './borrowing-history.component.html',
  styleUrls: ['./borrowing-history.component.css']
})
export class BorrowHistoryComponent implements OnInit {
  borrowHistory: any[] = []; // Borrowing history data
  filteredHistory: any[] = []; // Filtered data for display
  searchForm: FormGroup; // Search form
  borrowRecords: BorrowRecord[] = [];
  displayedColumns: string[] = ['userName', 'bookTitle', 'borrowDate', 'returnDate', 'actions'];
  searchUserName = '';
  searchBookTitle = '';

  constructor(private borrowingService: BorrowingHistoryService, private fb: FormBuilder) {
    this.searchForm = this.fb.group({
      userId: [''],
      bookId: ['']
    });
  }

  ngOnInit(): void {
    // Load all borrowing history on init
    this.fetchAllHistory();
  }

  fetchAllHistory(): void {
    this.borrowingService.getBorrowHistory().subscribe({
      next: (history) => {
        this.borrowHistory = history;
        this.filteredHistory = history; // Initialize filtered data
      },
      error: (err) => {
        console.error('Error fetching borrowing history:', err);
      }
    });
  }

  search(): void {
    const { userId, bookId } = this.searchForm.value;

    // Filter by userId or bookId or both
    this.filteredHistory = this.borrowHistory.filter((record) => {
      const matchesUser = !userId || record.userId === +userId;
      const matchesBook = !bookId || record.bookId === +bookId;
      return matchesUser && matchesBook;
    });
  }

  getAllBorrowRecords(): void {
    this.borrowingService.searchBorrowHistory(this.searchUserName, this.searchBookTitle).subscribe({
      next: (records) => (this.borrowRecords = records),
      error: (err) => console.error('Error fetching borrow history:', err),
    });
  }

  onSearch(): void {
    this.getAllBorrowRecords();
  }

  // returnBook(borrowId: number): void {
  //   console.log(borrowId)
  //   if (borrowId == null || isNaN(borrowId)) {
  //     console.error('Invalid borrowId:', borrowId);
  //     return;
  //   }
  
  //   this.borrowingService.returnBook(borrowId).subscribe(
  //     (response) => {
  //       // Handle successful book return
  //       console.log('Book returned successfully');
  //     },
  //     (error) => {
  //       console.error('Error returning book', error);
  //     }
  //   );
  // }

  returnBook(borrowId: number, record: any): void {
    if (borrowId == null || isNaN(borrowId)) {
      console.error('Invalid borrowId:', borrowId);
      return;
    }
  
    this.borrowingService.returnBook(borrowId).subscribe(
      (response) => {
        console.log('Book returned successfully');
        // Update the return date and disable the button for the corresponding record
        record.returnDate = new Date().toISOString(); // Set current date as the return date
        // Optionally trigger UI update like fade effect or show a confirmation message
      },
      (error) => {
        console.error('Error returning book', error);
      }
    );
  }
  clearSearch(): void {
    this.searchForm.reset();
    this.filteredHistory = [...this.borrowHistory]; // Reset to full history
  }
}
