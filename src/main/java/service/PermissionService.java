package service;

import repository.Possessable;
import repository.entities.Issue;
import repository.entities.Permission;
import repository.entities.Role;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.util.List;

@ApplicationScoped
public class PermissionService {

    @PersistenceContext
    private EntityManager entityManager;

    @Inject
    private UserService userService;

    @Inject
    private IssueService issueService;

    public boolean hasUserPermissionToIssue(Long userId, Long issueId, String methodName) {
        List<Role> roles = userService.findUser(userId).getUserCredentials().getRoles();
        Issue issue = issueService.findIssue(issueId);

        if (issue == null) {
            return false;
        }

        return roles.stream().anyMatch(role -> {
            Permission permission = findPermissionByRoleAndMethod(role.getRoleName(), methodName);

            if (permission == null) {
                return false;
            }

            return checkPermission(userId, issue, permission);
        });
    }

    private boolean checkPermission(Long userId, Possessable possessable, Permission permission) {
        switch (permission.getPermissionLevel()) {
            case GRANTED:
                return true;
            case IF_OWNER:
                return possessable.getOwnerId().equals(userId);
            default:
                return false;
        }
    }

    private Permission findPermissionByRoleAndMethod(String roleName, String methodName) {
        List<Permission> permissions = entityManager.createNamedQuery("Permission.findByRoleAndMethod", Permission.class)
                .setParameter(1, roleName)
                .setParameter(2, methodName)
                .getResultList();

        if (permissions.isEmpty()) {
            return null;
        }

        return permissions.get(0);
    }
}
