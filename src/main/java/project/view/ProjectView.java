package project.view;

import issue.IssueService;
import issue.model.Issue;
import lombok.Getter;
import lombok.Setter;
import project.model.Project;

import javax.faces.view.ViewScoped;
import javax.inject.Inject;
import javax.inject.Named;
import java.io.Serializable;
import java.util.List;


@Named
@ViewScoped
public class ProjectView implements Serializable {

    @Getter
    @Setter
    private Project project;

    private final IssueService issueService;

    @Inject
    public ProjectView(IssueService issueService) {
        this.issueService = issueService;
    }

    public List<Issue> getIssues() {
        return issueService.findIssuesByProjectId(project.getId());
    }

    public String removeIssue(Issue issue) {
        issueService.removeIssue(issue);
        return "project_view?faces-redirect=true&includeViewParams=true";
    }
}
