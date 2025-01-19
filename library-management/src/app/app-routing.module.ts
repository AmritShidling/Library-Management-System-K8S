import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';
import { RouterModule, Routes } from '@angular/router';
import { DashboardComponent } from './dashboard/dashboard.component';
import { BooksComponent } from './books/books.component';
import { BorrowingComponent } from './borrowing/borrowing.component';
import { BorrowHistoryComponent } from './borrowing-history/borrowing-history.component';
import { UsersComponent } from './users/users.component';


const routes: Routes = [
  // { path: '', redirectTo: '/borrow-history', pathMatch: 'full' }, // Default to Borrow History
  { path: 'borrow-history', component: BorrowHistoryComponent },
  { path: 'borrow-book', component: BorrowingComponent },
  { path: 'users', component: UsersComponent },
  { path: 'books', component: BooksComponent },

];
// const routes: Routes = [
//   { path: '', redirectTo: '/books', pathMatch: 'full' }, // Redirect default route to /books
//   { path: 'books', component: BooksComponent },          // Route for BooksComponent
// ];

@NgModule({
  imports: [RouterModule.forRoot(routes)],
  exports: [RouterModule]
})
export class AppRoutingModule { }
