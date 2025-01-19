import { Component, OnInit } from '@angular/core';
import { MatDialog } from '@angular/material/dialog';
import { UsersService, User } from 'src/app/services/users.service';
import { UserDialogComponent } from './user-dialog/user-dialog.component';

@Component({
  selector: 'app-users',
  templateUrl: './users.component.html',
  styleUrls: ['./users.component.css']
})
export class UsersComponent implements OnInit {
  displayedColumns: string[] = ['id', 'name', 'email', 'actions'];
  users: User[] = [];

  constructor(private usersService: UsersService, private dialog: MatDialog) {}

  ngOnInit(): void {
    this.loadUsers();
  }

  // Load all users
  loadUsers(): void {
    this.usersService.getAllUsers().subscribe({
      next: (data) => (this.users = data),
      error: (err) => console.error('Error fetching users:', err),
    });
  }

  // Open dialog for adding a new user
  addUser(): void {
    const dialogRef = this.dialog.open(UserDialogComponent, {
      width: '400px',
      data: { isEdit: false },
    });

    dialogRef.afterClosed().subscribe((result) => {
      if (result) {
        this.loadUsers(); // Reload users after adding
      }
    });
  }

  // Open dialog for editing an existing user
  editUser(user: User): void {
    const dialogRef = this.dialog.open(UserDialogComponent, {
      width: '400px',
      data: { isEdit: true, user },
    });

    dialogRef.afterClosed().subscribe((result) => {
      if (result) {
        this.loadUsers(); // Reload users after editing
      }
    });
  }
}
