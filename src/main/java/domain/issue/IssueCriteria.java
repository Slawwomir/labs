package domain.issue;

import lombok.Builder;
import lombok.Data;

@Builder
@Data
public class IssueCriteria {
    private IssueType issueType;
    private IssueStatus issueStatus;
    private Long reporterId;
    private Long assigneeId;
    private Long projectId;
    private String name;
}
