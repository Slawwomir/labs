package repository.entities;

import lombok.Data;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
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
public class User implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull
    private String name;

    @OneToMany(fetch = FetchType.EAGER, mappedBy = "projectOwner", cascade = CascadeType.ALL)
    private List<Project> projects;

    @OneToOne(mappedBy = "user")
    private UserCredentials userCredentials;
}
