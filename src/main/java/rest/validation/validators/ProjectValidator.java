package rest.validation.validators;

import rest.validation.annotations.ProjectExists;
import service.ProjectService;

import javax.inject.Inject;
import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

public class ProjectValidator implements ConstraintValidator<ProjectExists, Long> {

    @Inject
    private ProjectService projectService;

    @Override
    public void initialize(ProjectExists constraintAnnotation) {
    }

    @Override
    public boolean isValid(Long value, ConstraintValidatorContext context) {
        if (value == null) {
            return true;
        }

        return projectService.findProject(value) != null;
    }
}
