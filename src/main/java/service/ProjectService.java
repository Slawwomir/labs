package service;

import repository.entities.Issue;
import repository.entities.Project;
import repository.entities.User;
import rest.dto.project.ProjectDTO;

import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.util.List;

@Stateless
public class ProjectService {

    @PersistenceContext
    private EntityManager entityManager;

    public List<Project> findAllProjects() {
        return entityManager.createNamedQuery("Project.findAll", Project.class).getResultList();
    }

    public Project findProject(Long id) {
        if (id == null) {
            return null;
        }

        return entityManager.find(Project.class, id);
    }

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

    public void removeProject(Long projectId) {
        entityManager.remove(findProject(projectId));
    }

    public Issue findIssueFromProject(Long projectId, Long issueId) {
        return entityManager.createNamedQuery("Issue.findByIssueIdAndProjectId", Issue.class).setParameter(1, issueId).setParameter(2, projectId).getSingleResult();
    }
}
