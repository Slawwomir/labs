package service;

import repository.entities.Project;
import repository.entities.User;
import rest.dto.project.ProjectDTO;

import javax.ejb.Stateful;
import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.transaction.Transactional;
import java.util.List;


@Stateful
public class ProjectService {

    @PersistenceContext
    private EntityManager entityManager;

    @Inject
    private IssueService issueService;

    @Transactional
    public List<Project> findAllProjects() {
        return entityManager.createNamedQuery("Project.findAll", Project.class).getResultList();
    }

    @Transactional
    public Project findProject(Long id) {
        return entityManager.find(Project.class, id);
    }

    @Transactional
    public synchronized Project saveProject(Project project) {
        if (project.getId() != null) {
            entityManager.merge(project);
        } else {
            entityManager.persist(project);
        }

        return project;
    }

    public Project saveProject(ProjectDTO projectDTO) {
        Project project = new Project();
        project.setId(projectDTO.getId());
        project.setName(projectDTO.getName());

        if(projectDTO.getProjectOwnerId() != null) {
            project.setProjectOwner(entityManager.find(User.class, projectDTO.getProjectOwnerId()));
        }

        return saveProject(project);
    }

    @Transactional
    public void removeProject(Project project) {
        Project toRemove = entityManager.contains(project) ? project : entityManager.merge(project);
        entityManager.remove(toRemove);
    }

    public void removeProject(Long projectId) {
        removeProject(findProject(projectId));
    }
}
