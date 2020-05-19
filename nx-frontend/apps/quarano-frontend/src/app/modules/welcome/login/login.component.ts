import { Component, OnInit } from '@angular/core';
import { FormControl, FormGroup, Validators } from '@angular/forms';
import { Router } from '@angular/router';
import { filter, take } from 'rxjs/operators';
import { MatInput } from '@angular/material/input';
import {EnrollmentService} from '../../../services/enrollment.service';
import {UserService} from '../../../services/user.service';
import {SnackbarService} from '../../../services/snackbar.service';

@Component({
  selector: 'qro-login',
  templateUrl: './login.component.html',
  styleUrls: ['./login.component.scss']
})
export class LoginComponent implements OnInit {

  public loginFormGroup = new FormGroup({
    username: new FormControl(null, Validators.required),
    password: new FormControl(null, Validators.required)
  });

  constructor(
    private userService: UserService,
    private enrollmentService: EnrollmentService,
    private snackbarService: SnackbarService,
    private router: Router) {
  }

  ngOnInit(): void {
    this.userService.isLoggedIn$.pipe(
      take(1),
      filter((loggedin) => loggedin)
    ).subscribe(() => {
      this.userService.logout();
    });
  }

  public submitForm() {
    this.userService.login(this.loginFormGroup.controls.username.value, this.loginFormGroup.controls.password.value)
      .subscribe(
        _ => {
          this.snackbarService.success('Willkommen bei quarano');
          if (this.userService.isHealthDepartmentUser) {
            this.router.navigate(['/tenant-admin']);
          } else {
            this.enrollmentService.loadEnrollmentStatus().subscribe(() => {
              this.router.navigate(['/diary']);
            });
          }
        },
        () => {
          this.snackbarService.error('Benutzername oder Passwort falsch');
        }
      );
  }

  trimValue(input: MatInput) {
    input.value = input.value.trim();
  }
}
