package rest.validation.validators;

import rest.validation.annotations.IssueExists;
import service.IssueService;

import javax.inject.Inject;
import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

public class IssueValidator implements ConstraintValidator<IssueExists, Long> {

    @Inject
    private IssueService issueService;

    @Override
    public boolean isValid(Long id, ConstraintValidatorContext context) {
        if (id == null) {
            return true;
        }

        return issueService.findIssue(id) != null;
    }
}
