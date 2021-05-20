import {FormControl, FormGroup, ValidationErrors} from "@angular/forms";

export class FormValidator {

  static dateLessThan(end: FormControl, start: FormControl): ValidationErrors {
    // check is endTime less than startTime
    if(end < start) {
      return { 'dateLessThan': true}
    }
    return {};
  }

}
