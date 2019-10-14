package service;

import repository.entities.Project;
import repository.entities.User;
import rest.dto.project.ProjectDTO;

import javax.ejb.Stateful;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.transaction.Transactional;
import java.util.List;


@Stateful
public class ProjectService {

    @PersistenceContext
    private EntityManager entityManager;

    public List<Project> findAllProjects() {
        return entityManager.createNamedQuery("Project.findAll", Project.class).getResultList();
    }

    public Project findProject(Long id) {
        return entityManager.find(Project.class, id);
    }

    @Transactional
    public Project saveProject(Project project) {
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
        project.setProjectOwner(entityManager.find(User.class, projectDTO.getProjectOwnerId()));

        return saveProject(project);
    }

    public void removeProject(Project project) {
        removeProject(project.getId());
    }

    @Transactional
    public void removeProject(Long projectId) {
        entityManager.createNamedQuery("Project.remove", Project.class).setParameter(1, projectId);
    }
}
