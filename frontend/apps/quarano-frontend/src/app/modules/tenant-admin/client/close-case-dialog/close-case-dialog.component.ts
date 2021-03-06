import {
  ValidationErrorGenerator,
  TrimmedPatternValidator,
  VALIDATION_PATTERNS
} from '@quarano-frontend/shared/util-form-validation';
import { Component, Inject } from '@angular/core';
import { MAT_DIALOG_DATA, MatDialogRef } from '@angular/material/dialog';
import { FormControl, FormGroup, Validators } from '@angular/forms';

@Component({
  selector: 'qro-close-case-dialog',
  templateUrl: './close-case-dialog.component.html',
  styleUrls: ['./close-case-dialog.component.scss']
})
export class CloseCaseDialogComponent {
  errorGenerator = ValidationErrorGenerator;
  commentGroup = new FormGroup({
    comment: new FormControl('', [Validators.required, TrimmedPatternValidator.trimmedPattern(VALIDATION_PATTERNS.textual)])
  });

  constructor(
    @Inject(MAT_DIALOG_DATA)
    public data: {
      text: string;
      title: string;
    },
    private matDialogRef: MatDialogRef<CloseCaseDialogComponent>
  ) {
  }

  public confirm() {
    this.matDialogRef.close(this.commentGroup.get('comment').value);
  }

}
