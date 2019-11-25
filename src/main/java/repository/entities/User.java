package repository.entities;

import lombok.Data;
import repository.Possessable;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.util.List;

@Entity
@Data
@NamedQueries({
        @NamedQuery(name = "User.findAll", query = "SELECT u FROM User u"),
        @NamedQuery(name = "User.remove", query = "DELETE FROM User u where u.id = ?1"),
        @NamedQuery(name = "User.findUserByName", query = " SELECT u FROM User u where u.name = ?1")
})
@NamedEntityGraphs({
        @NamedEntityGraph(
                name = "User.Graphs.withProjects",
                attributeNodes = {@NamedAttributeNode("projects")}
        )
})
public class User implements Serializable, Possessable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull
    private String name;

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "projectOwner", cascade = CascadeType.REMOVE)
    private List<Project> projects;

    @OneToOne(mappedBy = "user")
    private UserCredentials userCredentials;

    @Override
    public Long getOwnerId() {
        return id;
    }
}
