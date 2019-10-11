package rest.model.issue;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.ws.rs.core.Link;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter;
import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@XmlRootElement
public class Issue {
    private Long id;
    private IssueType type;
    private IssueStatus status;
    private String name;
    private String description;
    private Long projectId;

    private Long reporterId;
    private Long assigneeId;

    @XmlElement
    @XmlJavaTypeAdapter(Link.JaxbAdapter.class)
    private List<Link> links;

    public Issue(Issue issue) {
        this.id = issue.id;
        this.type = issue.type;
        this.status = issue.status;
        this.name = issue.name;
        this.description = issue.description;
        this.projectId = issue.projectId;
        this.reporterId = issue.reporterId;
        this.assigneeId = issue.assigneeId;
    }
}
