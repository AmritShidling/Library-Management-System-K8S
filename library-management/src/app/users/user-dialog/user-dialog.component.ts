import { Component, Inject } from '@angular/core';
import { MatDialogRef, MAT_DIALOG_DATA } from '@angular/material/dialog';
import { FormBuilder, FormGroup, Validators } from '@angular/forms';
import { UsersService } from 'src/app/services/users.service';

@Component({
  selector: 'app-user-dialog',
  templateUrl: './user-dialog.component.html',
  styleUrls: ['./user-dialog.component.css']
})
export class UserDialogComponent {
  userForm: FormGroup;
  errorMessage: any;

  constructor(
    private dialogRef: MatDialogRef<UserDialogComponent>,
    @Inject(MAT_DIALOG_DATA) public data: any,
    private fb: FormBuilder,
    private usersService: UsersService
  ) {
    this.userForm = this.fb.group({
      name: ['', Validators.required],
      email: ['', [Validators.required, Validators.email]],
    });

    if (this.data.isEdit) {
      this.userForm.patchValue(this.data.user);
    }
  }

  onCancel(): void {
    this.dialogRef.close();
  }

  onSubmit(): void {
    if (this.userForm.valid) {
      const userData = this.userForm.value;
  
      if (this.data.isEdit) {
        // Update existing user
        this.usersService.updateUser(this.data.user.id, userData).subscribe({
          next: (updatedUser) => {
            this.dialogRef.close(updatedUser); // Close the dialog and pass updated user
          },
          error: (err) => {
            if (err.status === 400) {
              this.errorMessage = err.error.message || 'An error occurred.';
            } else {
              this.errorMessage = 'Something went wrong. Please try again.';
            }
          },
        });
      } else {
        // Create new user
        this.usersService.createUser(userData).subscribe({
          next: (createdUser) => {
            this.dialogRef.close(createdUser); // Close the dialog and pass created user
          },
          error: (err) => {
            if (err.status === 400) {
              this.errorMessage = err.error.message || 'An error occurred.';
            } else {
              this.errorMessage = 'Something went wrong. Please try again.';
            }
          },
        });
      }
    }
  }
  

}
