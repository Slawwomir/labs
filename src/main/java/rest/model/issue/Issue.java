package rest.model.issue;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

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
public class Issue {
    @XmlElement
    private Long id;
    @XmlElement
    private IssueType type;
    @XmlElement
    private IssueStatus status;
    @XmlElement
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

    public Issue(Issue issue) {
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
}
