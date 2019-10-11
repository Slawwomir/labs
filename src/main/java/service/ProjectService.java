package service;

import rest.model.project.Project;

import javax.annotation.PostConstruct;
import javax.enterprise.context.ApplicationScoped;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;


@ApplicationScoped
public class ProjectService {

    private final List<Project> projects = new ArrayList<>();

    @PostConstruct
    public void init() {
        projects.add(new Project(1L, "Labki z Javy JEE"));
        projects.add(new Project(2L, "Praca inżynierska"));
    }

    public List<Project> findAllProjects() {
        return projects.stream().map(Project::new).collect(Collectors.toList());
    }

    public Project findProject(Long id) {
        return projects.stream().filter(p -> p.getId().equals(id)).findFirst().orElse(null);
    }

    public synchronized Project saveProject(Project project) {
        if (project.getId() != null) {
            projects.removeIf(p -> p.getId().equals(project.getId()));
            projects.add(project);
        } else {
            project.setId(projects.stream().mapToLong(Project::getId).max().orElse(0) + 1);
            projects.add(new Project(project));
        }

        return project;
    }

    public void removeProject(Project project) {
        removeProject(project.getId());
    }

    public void removeProject(Long projectId) {
        projects.removeIf(p -> p.getId().equals(projectId));
    }
}
