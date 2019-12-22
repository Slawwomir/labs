package repository.entities;

import domain.issue.IssueStatus;
import domain.issue.IssueType;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import repository.Possessable;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.util.Date;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@NamedQueries({
        @NamedQuery(name = "Issue.findAll",
                query = "SELECT i FROM Issue i ORDER BY i.updatedDate desc"),
        @NamedQuery(name = "Issue.findByProjectId",
                query = "SELECT i FROM Issue i WHERE i.project.id = ?1 ORDER BY i.updatedDate desc"),
        @NamedQuery(name = "Issue.findByReporterId",
                query = "SELECT i FROM Issue i WHERE i.reporter.id = ?1 ORDER BY i.updatedDate desc"),
        @NamedQuery(name = "Issue.findByProjectIdAndStatus",
                query = "SELECT i FROM Issue i where i.project.id = ?1 and i.status = ?2 ORDER BY i.updatedDate desc"),
        @NamedQuery(name = "Issue.remove",
                query = "DELETE FROM Issue i where i.id = ?1"),
        @NamedQuery(name = "Issue.findByIssueIdAndProjectId",
                query = "SELECT i FROM Issue i WHERE i.id = ?1 and i.project.id = ?2 ORDER BY i.updatedDate desc")
})
@Builder
public class Issue implements Serializable, Possessable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull
    private String name;

    @NotNull
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "reporter_id")
    private User reporter;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "assignee_id")
    private User assignee;

    @NotNull
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "project_id")
    private Project project;

    @Enumerated(EnumType.STRING)
    private IssueType type;

    @Enumerated(EnumType.STRING)
    private IssueStatus status;

    @Column(name = "updated_date")
    private Date updatedDate;

    public String description;

    @Override
    public Long getOwnerId() {
        return reporter.getId();
    }
}
