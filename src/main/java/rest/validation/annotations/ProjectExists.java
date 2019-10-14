package rest.validation.annotations;

import rest.validation.validators.ProjectValidator;

import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target({ElementType.FIELD})
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = {ProjectValidator.class})
public @interface ProjectExists {
    Class<?>[] groups() default { };
    Class<? extends Payload>[] payload() default { };
    String message() default "Project doesn't exist.";
}
