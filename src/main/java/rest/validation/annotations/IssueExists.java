package rest.validation.annotations;


import rest.validation.validators.IssueValidator;

import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target({ElementType.FIELD, ElementType.PARAMETER})
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = {IssueValidator.class})
public @interface IssueExists {
    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};

    String message() default "Issue doesn't exist.";
}
