package service;

import repository.entities.Issue;
import domain.issue.IssueStatus;
import domain.issue.IssueType;
import repository.entities.Project;
import rest.dto.issue.IssueDTO;

import javax.ejb.Stateful;
import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.transaction.Transactional;
import java.util.List;


@Stateful
public class IssueService {

    @PersistenceContext
    private EntityManager entityManager;

    @Inject
    private UserService userService;

    @Transactional
    public List<Issue> findAllIssues() {
        return entityManager.createNamedQuery("Issue.findAll", Issue.class).getResultList();
    }

    @Transactional
    public Issue findIssue(Long id) {
        return entityManager.find(Issue.class, id);
    }

    @Transactional
    public List<Issue> findIssuesByProjectId(Long projectId) {
        return entityManager.createNamedQuery("Issue.findByProjectId", Issue.class).setParameter(1, projectId).getResultList();
    }

    @Transactional
    public synchronized Issue saveIssue(Issue issue) {
        if (issue.getId() != null) {
            entityManager.merge(issue);
        } else {
            entityManager.persist(issue);
        }

        return issue;
    }

    @Transactional
    public void removeIssue(Issue issue) {
        Issue toRemove = entityManager.contains(issue) ? issue : entityManager.merge(issue);
        entityManager.remove(toRemove);
    }

    public void removeIssue(Long issueId) {
        Issue issue = new Issue();
        issue.setId(issueId);
        removeIssue(issue);
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

        if (issueDTO.getProjectId() != null) {
            issue.setProject(entityManager.find(Project.class, issueDTO.getProjectId()));
        }

        if (issueDTO.getAssigneeId() != null) {
            issue.setAssignee(userService.findUser(issueDTO.getAssigneeId()));
        }

        if (issueDTO.getReporterId() != null) {
            issue.setReporter(userService.findUser(issueDTO.getReporterId()));
        }

        issue.setDescription(issueDTO.getDescription());
        issue.setName(issueDTO.getName());
        issue.setStatus(issueDTO.getStatus());
        issue.setType(issueDTO.getType());

        return saveIssue(issue);
    }
}
