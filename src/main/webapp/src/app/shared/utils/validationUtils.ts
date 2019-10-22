import {HttpErrorResponse} from "@angular/common/http";
import {PermissionLevel} from "../../models/permissionLevel";

export class ValidationUtils {
  static mapErrors(error): String[] {
    if (error instanceof HttpErrorResponse) {
      const errorMessages = [];

      if (error.error && error.error.parameterViolations) {
        error.error.parameterViolations.forEach(err => {
          errorMessages[err.path.split(".")[2]] = err.message;
        });
      }
      return errorMessages;
    }
  }

  static validatePermissions(permissions: PermissionLevel[], ownerId: number = undefined, currentUserId: number = undefined) {
    if (!permissions) {
      return false;
    }

    if (permissions.indexOf(PermissionLevel.GRANTED) != -1) {
      return true;
    }

    if (permissions.indexOf(PermissionLevel.IF_OWNER) != -1) {
      if (!ownerId) {
        return true;
      }
      return ownerId == currentUserId;
    }

    return false;
  }
}
