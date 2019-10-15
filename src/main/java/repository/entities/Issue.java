package repository.entities;

import domain.issue.IssueStatus;
import domain.issue.IssueType;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.validation.constraints.NotNull;
import java.io.Serializable;

@Entity
@Getter
@Setter
@NamedQueries({
        @NamedQuery(name = "Issue.findAll", query = "SELECT i FROM Issue i"),
        @NamedQuery(name = "Issue.findByProjectId", query = "SELECT i FROM Issue i WHERE i.project.id = ?1"),
        @NamedQuery(name = "Issue.findByReporterId", query = "SELECT i FROM Issue i WHERE i.reporter.id = ?1"),
        @NamedQuery(name = "Issue.findByProjectIdAndStatus", query = "SELECT i FROM Issue i where i.project.id = ?1 and i.status = ?2"),
        @NamedQuery(name = "Issue.remove", query = "DELETE FROM Issue i where i.id = ?1"),
        @NamedQuery(name = "Issue.findByIssueIdAndProjectId", query = "SELECT i FROM Issue i WHERE i.id = ?1 and i.project.id = ?2")
})
public class Issue implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull
    private String name;

    @NotNull
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
