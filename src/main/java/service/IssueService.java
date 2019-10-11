package service;

import rest.model.issue.Issue;
import rest.model.issue.IssueStatus;
import rest.model.issue.IssueType;

import javax.annotation.PostConstruct;
import javax.enterprise.context.ApplicationScoped;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;


@ApplicationScoped
public class IssueService {

    private final List<Issue> issues = new ArrayList<>();

    @PostConstruct
    public void init() {
        issues.add(new Issue(1L, IssueType.TASK, IssueStatus.OPEN, "Zrób labkę 1", "No pierwsza to jest taka spoko, więc ją zrób", 1L, 1L, null, null));
        issues.add(new Issue(2L, IssueType.TASK, IssueStatus.OPEN, "Zrób labkę 1", "No pierwsza to jest taka spoko, więc ją zrób", 1L, 1L, null, null));
        issues.add(new Issue(3L, IssueType.TASK, IssueStatus.OPEN, "Zrób labkę 1", "No pierwsza to jest taka spoko, więc ją zrób", 1L, 1L, null, null));
        issues.add(new Issue(4L, IssueType.BUG, IssueStatus.OPEN, "Nie działa strona", "Jak się wejdzie, to nie działa. Może nie wchodzić?", 2L, 1L, null, null));
        issues.add(new Issue(5L, IssueType.BUG, IssueStatus.OPEN, "Nie działa strona", "Jak się wejdzie, to nie działa. Może nie wchodzić?", 2L, 1L, null, null));
    }

    public List<Issue> findAllIssues() {
        return issues.stream().map(Issue::new).collect(Collectors.toList());
    }

    public Issue findIssue(Long id) {
        return issues.stream().filter(i -> i.getId().equals(id)).findFirst().orElse(null);
    }

    public List<Issue> findIssuesByProjectId(Long projectId) {
        return issues.stream().filter(i -> i.getProjectId().equals(projectId)).collect(Collectors.toList());
    }

    public synchronized Issue saveIssue(Issue issue) {
        if (issue.getId() != null) {
            issues.removeIf(i -> i.getId().equals(issue.getId()));
            issues.add(new Issue(issue));
        } else {
            issue.setId(issues.stream().mapToLong(Issue::getId).max().orElse(0) + 1);
            issues.add(new Issue(issue));
        }

        return issue;
    }

    public void removeIssue(Issue issue) {
        removeIssue(issue.getId());
    }

    public void removeIssue(Long issueId) {
        issues.removeIf(issue -> issue.getId().equals(issueId));
    }

    public List<IssueStatus> getIssueStatuses() {
        return List.of(IssueStatus.values());
    }

    public List<IssueType> getIssueTypes() {
        return List.of(IssueType.values());
    }
}
