import {
  ValidationErrorGenerator,
  PasswordValidator,
  ConfirmValidPasswordMatcher
} from '@quarano-frontend/shared/util-form-validation';
import { BadRequestService } from '@quarano-frontend/shared/util-error';
import { AuthService } from '@quarano-frontend/auth/domain';
import { MatInput } from '@angular/material/input';
import { UserService } from './../../../../../../apps/quarano-frontend/src/app/services/user.service';
import { SubSink } from 'subsink';
import { SnackbarService } from './../../../../../../apps/quarano-frontend/src/app/services/snackbar.service';
import { Component, OnInit, OnDestroy } from '@angular/core';
import { FormGroup, FormControl, Validators } from '@angular/forms';
import { Router } from '@angular/router';

@Component({
  selector: 'qro-change-password',
  templateUrl: './change-password.component.html',
  styleUrls: ['./change-password.component.scss']
})
export class ChangePasswordComponent implements OnInit, OnDestroy {
  loading = false;
  formGroup: FormGroup;
  confirmValidParentMatcher = new ConfirmValidPasswordMatcher();
  private subs = new SubSink();
  errorGenerator = ValidationErrorGenerator;

  constructor(
    private userService: UserService,
    private snackbarService: SnackbarService,
    private router: Router,
    private authService: AuthService,
    private badRequestService: BadRequestService) { }

  ngOnInit() {
    this.createForm();
    this.subs.add(this.userService.user$
      .subscribe(user => this.formGroup.controls.username.setValue(user.username)));
  }

  ngOnDestroy() {
    this.subs.unsubscribe();
  }

  createForm() {
    this.formGroup = new FormGroup({
      username: new FormControl({ value: '', disabled: true }),
      current: new FormControl(null, [
        Validators.required]),
      password: new FormControl(null, [
        Validators.required, PasswordValidator.secure
      ]),
      passwordConfirm: new FormControl(null, [
        Validators.required
      ]),
    }, {
      validators: [PasswordValidator.mustMatch, PasswordValidator.mustNotIncludeUsername]
    });
  }

  submitForm() {
    if (this.formGroup.valid) {
      this.loading = true;
      this.subs.add(this.authService.changePassword(this.formGroup.value)
        .subscribe(() => {
          this.snackbarService.success('Ihr Passwort wurde geändert');
          this.router.navigate(['/welcome']);
        }, error => {
          this.badRequestService.handleBadRequestError(error, this.formGroup);
        }).add(() => this.loading = false));
    }
  }

  trimValue(input: MatInput) {
    input.value = input.value.trim();
  }

}
