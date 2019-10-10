package rest.model.issue;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class Issue {
    private Long id;
    private IssueType type;
    private IssueStatus status;
    private String name;
    private String description;
    private Long projectId;

    public Issue(Issue issue) {
        this.id = issue.id;
        this.type = issue.type;
        this.status = issue.status;
        this.name = issue.name;
        this.description = issue.description;
        this.projectId = issue.projectId;
    }
}
