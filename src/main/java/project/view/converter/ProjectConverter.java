package project.view.converter;

import org.apache.commons.lang3.StringUtils;
import project.ProjectService;
import project.model.Project;

import javax.enterprise.context.Dependent;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.convert.Converter;
import javax.faces.convert.FacesConverter;
import javax.inject.Inject;


@FacesConverter(forClass = Project.class, managed = true)
@Dependent
public class ProjectConverter implements Converter<Project> {

    private final ProjectService projectService;

    @Inject
    public ProjectConverter(ProjectService projectService) {
        this.projectService = projectService;
    }

    @Override
    public Project getAsObject(FacesContext facesContext, UIComponent uiComponent, String s) {
        if (!StringUtils.isNumeric(s)) {
            return new Project();
        }
        return projectService.findProject(Long.valueOf(s));
    }

    @Override
    public String getAsString(FacesContext facesContext, UIComponent uiComponent, Project project) {
        if (project == null || project.getId() == null) {
            return "";
        }

        return Long.toString(project.getId());
    }
}
