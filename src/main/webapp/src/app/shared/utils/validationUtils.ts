import {HttpErrorResponse} from "@angular/common/http";

export class ValidationUtils {
  static mapErrors(error): String[] {
    if (error instanceof HttpErrorResponse) {
      const errorMessages = [];
      error.error.parameterViolations.forEach(err => {
        errorMessages[err.path.split(".")[2]] = err.message;
      });

      return errorMessages;
    }
  }
}
