package repository.entities;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import repository.Possessable;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.util.List;

@Entity
@NamedQueries({
        @NamedQuery(name = "Project.findAll", query = "SELECT p FROM Project p"),
        @NamedQuery(name = "Project.remove", query = "DELETE FROM Project p where p.id = ?1")
})
@NamedEntityGraphs({
        @NamedEntityGraph(
                name = "Project.Graphs.withIssues",
                attributeNodes = {@NamedAttributeNode("issues")}
        )
})
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Project implements Serializable, Possessable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull
    private String name;

    @NotNull
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "project_owner_id")
    private User projectOwner;

    @OneToMany(fetch = FetchType.LAZY, cascade = CascadeType.REMOVE, mappedBy = "project")
    private List<Issue> issues;

    @Override
    public Long getOwnerId() {
        return projectOwner.getId();
    }
}
