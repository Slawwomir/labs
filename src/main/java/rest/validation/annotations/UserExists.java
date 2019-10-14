package rest.validation.annotations;

import rest.validation.validators.UserValidator;

import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target({ElementType.FIELD})
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = {UserValidator.class})
public @interface UserExists {
    Class<?>[] groups() default { };
    Class<? extends Payload>[] payload() default { };

    String message() default "User doesn't exist.";
}
