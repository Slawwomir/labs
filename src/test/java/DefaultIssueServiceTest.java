import domain.issue.IssueCriteria;
import domain.issue.IssueStatus;
import domain.issue.IssueType;
import org.junit.jupiter.api.Test;
import repository.entities.Issue;
import rest.dto.issue.IssueDTO;

import java.util.List;
import java.util.stream.Collectors;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;

public class DefaultIssueServiceTest extends ServicesTest {

    private Issue createIssue(Long projectId, Long reporterId, String name, String description, IssueType issueType, IssueStatus issueStatus) {
        entityManager.getTransaction().begin();

        IssueDTO issueDTO = new IssueDTO();
        issueDTO.setProjectId(projectId);
        issueDTO.setReporterId(reporterId);
        issueDTO.setName(name);
        issueDTO.setDescription(description);
        issueDTO.setStatus(issueStatus);
        issueDTO.setType(issueType);
        Issue issue = issueService.saveIssue(issueDTO);

        entityManager.getTransaction().commit();
        return issue;
    }

    @Test
    public void testFindIssueById() {
        Issue issue = createIssue(1L, 1L, "Issue", "Desc", null, null);

        Issue foundIssue = issueService.findIssue(issue.getId());

        assertThat(issue.getId(), equalTo(foundIssue.getId()));
        assertThat(issue.getDescription(), equalTo(foundIssue.getDescription()));
        assertThat(issue.getProject().getId(), equalTo(foundIssue.getProject().getId()));
    }

    @Test
    public void testUpdateIssue() {
        createIssue(1L, 1L, "Issue", "Desc", null, null);

        Issue issue = issueService.findIssue(1L);
        issue.setDescription("Zmienione");

        entityManager.getTransaction().begin();
        issueService.saveIssue(issue);
        entityManager.getTransaction().commit();

        issue = issueService.findIssue(1L);
        assertThat(issue.getDescription(), equalTo("Zmienione"));
    }

    @Test
    public void testRemoveIssue() {
        createIssue(1L, 1L, "Issue", "Desc", null, null);

        entityManager.getTransaction().begin();
        issueService.removeIssue(1L);
        entityManager.getTransaction().commit();

        assertThat(issueService.findAllIssues(), empty());
    }

    @Test
    public void testFilterIssueByCriteria() {
        createIssue(1L, 1L, "Issue1", "Desc1", IssueType.BUG, IssueStatus.DONE);
        createIssue(2L, 2L, "Issue2", "Desc2", IssueType.STORY, IssueStatus.DONE);
        createIssue(1L, 1L, "Issue3", "Desc3", IssueType.BUG, IssueStatus.OPEN);
        createIssue(2L, 2L, "Issue4", "Desc4", IssueType.BUG, IssueStatus.IN_PROGRESS);


        List<Issue> issues = issueService.findIssues(
                IssueCriteria.builder()
                        .issueType(IssueType.BUG)
                        .issueStatus(IssueStatus.DONE)
                        .build()
        );

        assertThat(issues.size(), equalTo(1));
        assertThat(issues.get(0).getName(), equalTo("Issue1"));

        issues = issueService.findIssues(
                IssueCriteria.builder()
                        .projectId(1L)
                        .build()
        );

        assertThat(issues.size(), equalTo(2));
        assertThat(issues.stream().map(Issue::getName).collect(Collectors.toList()), containsInAnyOrder("Issue1", "Issue3"));
    }

    @Test
    public void testIssuesByUpdatedDate() {
        createIssue(1L, 1L, "Issue1", "Desc1", IssueType.BUG, IssueStatus.DONE);
        createIssue(2L, 2L, "Issue2", "Desc2", IssueType.STORY, IssueStatus.DONE);
        createIssue(1L, 1L, "Issue3", "Desc3", IssueType.BUG, IssueStatus.OPEN);
        createIssue(2L, 2L, "Issue4", "Desc4", IssueType.BUG, IssueStatus.IN_PROGRESS);

        List<Issue> issues = issueService.findIssues(IssueCriteria.builder().build());

        assertThat(issues.stream().map(Issue::getName).collect(Collectors.toList()), contains("Issue4", "Issue3", "Issue2", "Issue1"));

        Issue issue = issueService.findIssue(2L);
        issue.setDescription("Tralala");

        entityManager.getTransaction().begin();
        issueService.saveIssue(issue);
        entityManager.getTransaction().commit();

        issues = issueService.findIssues(IssueCriteria.builder().build());
        assertThat(issues.stream().map(Issue::getName).collect(Collectors.toList()), contains("Issue2", "Issue4", "Issue3", "Issue1"));
    }
}
