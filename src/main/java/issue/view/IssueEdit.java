package issue.view;

import issue.IssueService;
import issue.model.Issue;
import issue.model.IssueStatus;
import issue.model.IssueType;
import lombok.Getter;
import lombok.Setter;
import project.ProjectService;
import project.model.Project;

import javax.faces.view.ViewScoped;
import javax.inject.Inject;
import javax.inject.Named;
import java.io.Serializable;
import java.util.List;


@Named
@ViewScoped
public class IssueEdit implements Serializable {

    @Getter
    @Setter
    private Issue issue = new Issue();

    private final IssueService issueService;
    private final ProjectService projectService;

    @Inject
    public IssueEdit(IssueService issueService, ProjectService projectService) {
        this.issueService = issueService;
        this.projectService = projectService;
    }

    public List<IssueType> getIssueTypes() {
        return List.of(IssueType.values());
    }

    public List<IssueStatus> getIssueStatuses() {
        return List.of(IssueStatus.values());
    }

    public List<Project> getAvailableProjects() {
        return projectService.findAllProjects();
    }

    public String saveIssue() {
        issueService.saveIssue(issue);
        return String.format("project_view?faces-redirect=true&project=%d", issue.getProjectId());
    }
}
