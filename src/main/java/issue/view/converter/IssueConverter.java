package issue.view.converter;

import issue.IssueService;
import issue.model.Issue;
import org.apache.commons.lang3.StringUtils;

import javax.enterprise.context.Dependent;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.convert.Converter;
import javax.faces.convert.FacesConverter;
import javax.inject.Inject;


@FacesConverter(forClass = Issue.class, managed = true)
@Dependent
public class IssueConverter implements Converter<Issue> {

    private final IssueService issueService;

    @Inject
    public IssueConverter(IssueService issueService) {
        this.issueService = issueService;
    }

    @Override
    public Issue getAsObject(FacesContext facesContext, UIComponent uiComponent, String s) {
        if(!StringUtils.isNumeric(s)) {
            return new Issue();
        }

        return issueService.findIssue(Long.valueOf(s));
    }

    @Override
    public String getAsString(FacesContext facesContext, UIComponent uiComponent, Issue issue) {
        if (issue == null) {
            return "";
        }

        return String.valueOf(issue.getId());
    }
}
