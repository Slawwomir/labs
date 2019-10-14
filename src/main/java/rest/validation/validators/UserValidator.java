package rest.validation.validators;

import rest.validation.annotations.UserExists;
import service.UserService;

import javax.inject.Inject;
import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

public class UserValidator implements ConstraintValidator<UserExists, Long> {

    @Inject
    private UserService userService;

    @Override
    public void initialize(UserExists constraintAnnotation) {

    }

    @Override
    public boolean isValid(Long value, ConstraintValidatorContext context) {
        if (value == null) {
            return true;
        }

        return userService.findUser(value) != null;
    }
}
