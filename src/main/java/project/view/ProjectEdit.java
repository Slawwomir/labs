package project.view;

import lombok.Getter;
import lombok.Setter;
import project.ProjectService;
import project.model.Project;

import javax.faces.view.ViewScoped;
import javax.inject.Inject;
import javax.inject.Named;
import java.io.Serializable;

@Named
@ViewScoped
public class ProjectEdit implements Serializable {

    @Getter
    @Setter
    private Project project = new Project();

    private final ProjectService projectService;

    @Inject
    public ProjectEdit(ProjectService projectService) {
        this.projectService = projectService;
    }

    public String saveProject() {
        projectService.saveProject(project);
        return "project_view?faces-redirect=true&includeViewParams=true";
    }

}
