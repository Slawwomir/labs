package repository.entities;

import lombok.Data;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.util.Date;
import java.util.List;
import java.util.Set;

@Data
@Entity
@Table(name = "USER_CREDENTIALS")
@NamedQuery(name = "UserCredentials.findByUserId", query = "SELECT uc FROM UserCredentials uc where uc.user.id = ?1")
public class UserCredentials implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @OneToOne
    @JoinColumn(name = "user_id")
    private User user;

    @NotNull
    @Column(name = "password_hash")
    private String passwordHash;

    @Column(name = "changed_date")
    private Date changedDate;

    @OneToMany(fetch = FetchType.EAGER, mappedBy = "userCredentials", cascade = CascadeType.ALL)
    private List<Role> roles;
}
