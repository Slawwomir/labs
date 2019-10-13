package repository.entities;

import domain.issue.IssueStatus;
import domain.issue.IssueType;
import lombok.Getter;
import lombok.Setter;
import rest.dto.issue.IssueDTO;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.io.Serializable;

@Entity
@Getter
@Setter
@NamedQueries({
        @NamedQuery(name = "Issue.findAll", query = "SELECT i FROM Issue i"),
        @NamedQuery(name = "Issue.findByProjectId", query = "SELECT i FROM Issue i WHERE i.project.id = ?1")
})
public class Issue implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull
    private String name;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "reporter_id")
    private User reporter;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "assignee_id")
    private User assignee;

    @NotNull
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "project_id")
    private Project project;

    @Enumerated(EnumType.STRING)
    private IssueType type;

    @Enumerated(EnumType.STRING)
    private IssueStatus status;

    private String description;
}
