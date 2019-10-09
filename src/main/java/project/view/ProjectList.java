package project.view;

import project.ProjectService;
import project.model.Project;

import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;
import javax.inject.Named;
import java.util.List;


@Named
@RequestScoped
public class ProjectList {

    private final ProjectService projectService;
    private List<Project> projects;

    @Inject
    public ProjectList(ProjectService projectService) {
        this.projectService = projectService;
    }

    public List<Project> getProjects() {
        if (projects == null) {
            projects = projectService.findAllProjects();
        }

        return projects;
    }

    public String removeProject(Project project) {
        projectService.removeProject(project);
        return "project_list?faces-redirect=true";
    }
}
