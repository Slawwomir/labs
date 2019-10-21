package repository.entities;

import domain.permission.PermissionLevel;
import lombok.Data;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.validation.constraints.NotNull;

@Entity
@Data
@NamedQueries({
        @NamedQuery(name = "Permission.findAll", query = "SELECT p FROM Permission p"),
        @NamedQuery(name = "Permission.findByRoleAndMethod", query = "SELECT p FROM Permission p where p.roleName = ?1 and p.methodName = ?2")
})
public class Permission {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull
    @Column(name = "role_name")
    private String roleName;

    @NotNull
    @Column(name = "method_name")
    private String methodName;

    @NotNull
    @Column(name = "permission_level")
    private PermissionLevel permissionLevel;
}
