package service;

import repository.entities.Issue;
import domain.issue.IssueStatus;
import domain.issue.IssueType;
import repository.entities.Project;
import rest.dto.issue.IssueDTO;

import javax.ejb.Stateless;
import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.util.List;


@Stateless
public class IssueService {

    @PersistenceContext
    private EntityManager entityManager;

    @Inject
    private UserService userService;

    public List<Issue> findAllIssues() {
        return entityManager.createNamedQuery("Issue.findAll", Issue.class).getResultList();
    }

    public Issue findIssue(Long id) {
        if (id == null) {
            return null;
        }
        return entityManager.find(Issue.class, id);
    }

    public List<Issue> findIssuesByProjectId(Long projectId) {
        return entityManager.createNamedQuery("Issue.findByProjectId", Issue.class).setParameter(1, projectId).getResultList();
    }

    public List<Issue> findIssuesByReporterId(Long reporterId) {
        return entityManager.createNamedQuery("Issue.findByReporterId", Issue.class).setParameter(1, reporterId).getResultList();
    }

    public synchronized Issue saveIssue(Issue issue) {
        if (issue.getId() != null) {
            entityManager.merge(issue);
        } else {
            entityManager.persist(issue);
        }

        return issue;
    }

    public void removeIssue(Long issueId) {
        entityManager.remove(findIssue(issueId));
    }

    public List<IssueStatus> getIssueStatuses() {
        return List.of(IssueStatus.values());
    }

    public List<IssueType> getIssueTypes() {
        return List.of(IssueType.values());
    }

    public Issue saveIssue(IssueDTO issueDTO) {
        Issue issue = new Issue();
        issue.setId(issueDTO.getId());
        issue.setProject(entityManager.find(Project.class, issueDTO.getProjectId()));
        issue.setReporter(userService.findUser(issueDTO.getReporterId()));
        issue.setAssignee(userService.findUser(issueDTO.getAssigneeId()));
        issue.setDescription(issueDTO.getDescription());
        issue.setName(issueDTO.getName());
        issue.setStatus(issueDTO.getStatus());
        issue.setType(issueDTO.getType());

        return saveIssue(issue);
    }

    public List<Issue> findIssuesByProjectIdAndStatus(Long projectId, IssueStatus status) {
        return entityManager.createNamedQuery("Issue.findByProjectIdAndStatus", Issue.class)
                .setParameter(1, projectId)
                .setParameter(2, status)
                .getResultList();
    }
}
