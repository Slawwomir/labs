package rest.dto.issue;

import domain.issue.IssueStatus;
import domain.issue.IssueType;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import repository.entities.Issue;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import javax.ws.rs.core.Link;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@XmlRootElement
public class IssueDTO {

    @XmlElement
    private Long id;

    @XmlElement
    @NotNull(message = "issue type must be set")
    private IssueType type;

    @XmlElement
    @NotNull(message = "issue status must be set")
    private IssueStatus status;

    @XmlElement
    @Size(min = 2, max = 20, message = "issue name must be between 2 and 20 characters")
    private String name;

    @XmlElement
    private String description;

    @XmlElement
    private Long projectId;

    @XmlElement
    private Long reporterId;

    @XmlElement
    private Long assigneeId;

    @XmlElement
    @XmlJavaTypeAdapter(Link.JaxbAdapter.class)
    private List<Link> links;

    public IssueDTO(IssueDTO issue) {
        this.id = issue.id;
        this.type = issue.type;
        this.status = issue.status;
        this.name = issue.name;
        this.description = issue.description;
        this.projectId = issue.projectId;
        this.reporterId = issue.reporterId;
        this.assigneeId = issue.assigneeId;
        this.links = issue.links == null ? Collections.emptyList() : new ArrayList<>(issue.links);
    }

    public IssueDTO(Issue issue) {
        this.id = issue.getId();
        this.type = issue.getType();
        this.status = issue.getStatus();
        this.name = issue.getName();
        this.description = issue.getDescription();
        this.projectId = issue.getProject().getId();
        this.reporterId = issue.getReporter() == null ? null : issue.getReporter().getId();
        this.assigneeId = issue.getAssignee() == null ? null : issue.getAssignee().getId();
        this.links = Collections.emptyList();
    }
}
