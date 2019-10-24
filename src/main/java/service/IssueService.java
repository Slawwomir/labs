package service;

import domain.issue.IssueChangedEvent;
import domain.issue.IssueCriteria;
import domain.issue.IssueStatus;
import domain.issue.IssueType;
import repository.entities.Issue;
import repository.entities.Project;
import rest.dto.issue.IssueDTO;

import javax.ejb.Stateless;
import javax.enterprise.event.Event;
import javax.enterprise.inject.Any;
import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import java.time.ZonedDateTime;
import java.util.Date;
import java.util.List;

@Stateless
public class IssueService {

    @PersistenceContext
    private EntityManager entityManager;

    @Inject
    private UserService userService;

    @Inject
    @Any
    private Event<IssueChangedEvent> issueChangedEvent;

    public List<Issue> findAllIssues() {
        return entityManager.createNamedQuery("Issue.findAll", Issue.class).getResultList();
    }

    public List<Issue> findIssues(int start, int max) {
        return entityManager.createNamedQuery("Issue.findAll", Issue.class)
                .setFirstResult(start)
                .setMaxResults(max)
                .getResultList();
    }

    public Long getIssuesCount() {
        return entityManager.createQuery("SELECT COUNT(i) FROM Issue i", Long.class).getSingleResult();
    }

    public Issue findIssue(Long id) {
        if (id == null) {
            return null;
        }
        return entityManager.find(Issue.class, id);
    }

    public List<Issue> findIssuesByProjectId(Long projectId) {
        return entityManager.createNamedQuery("Issue.findByProjectId", Issue.class)
                .setParameter(1, projectId)
                .getResultList();
    }

    public List<Issue> findIssuesByReporterId(Long reporterId) {
        return entityManager.createNamedQuery("Issue.findByReporterId", Issue.class)
                .setParameter(1, reporterId)
                .getResultList();
    }

    public synchronized Issue saveIssue(Issue issue) {
        issue.setUpdatedDate(Date.from(ZonedDateTime.now().toInstant()));

        if (issue.getId() != null) {
            entityManager.merge(issue);
        } else {
            entityManager.persist(issue);
        }

        issueChangedEvent.fire(new IssueChangedEvent(issue));

        return issue;
    }

    public void removeIssue(Long issueId) {
        Issue issue = findIssue(issueId);
        entityManager.remove(issue);

        issueChangedEvent.fire(new IssueChangedEvent(null));
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

    public List<Issue> getFilteredIssues(IssueCriteria issueCriteria) {
        CriteriaBuilder cr = entityManager.getCriteriaBuilder();
        Root<Issue> root = cr.createQuery().from(Issue.class);
        CriteriaQuery<Issue> issueCriteriaQuery = buildPredicate(issueCriteria, cr, root);

        return entityManager.createQuery(issueCriteriaQuery).getResultList();
    }

    private CriteriaQuery<Issue> buildPredicate(IssueCriteria issueCriteria, CriteriaBuilder cr, Root<Issue> root) {
        CriteriaQuery<Issue> select = cr.createQuery(Issue.class).select(root);

        if (issueCriteria.getAssigneeId() != null) {
            select = select.where(cr.equal(root.get("assignee").get("id"), issueCriteria.getAssigneeId()));
        }

        if (issueCriteria.getProjectId() != null) {
            select = select.where(cr.equal(root.get("project").get("id"), issueCriteria.getProjectId()));
        }

        if (issueCriteria.getName() != null) {
            select = select.where(cr.like(root.get("name"), issueCriteria.getName()));
        }

        if (issueCriteria.getIssueStatus() != null) {
            select = select.where(cr.equal(root.get("issue_status"), issueCriteria.getIssueStatus()));
        }

        if (issueCriteria.getIssueType() != null) {
            select = select.where(cr.equal(root.get("issue_type"), issueCriteria.getIssueType()));
        }

        if (issueCriteria.getReporterId() != null) {
            select = select.where(cr.equal(root.get("reporter").get("id"), issueCriteria.getReporterId()));
        }

        return select.orderBy(cr.desc(root.get("changed_date")));
    }
}
