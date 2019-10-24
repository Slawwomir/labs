package service;

import domain.permission.PermissionLevel;
import repository.Possessable;
import repository.entities.*;
import rest.dto.permission.PermissionDTO;

import javax.ejb.Stateless;
import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.util.List;

@Stateless
public class PermissionService {

    @PersistenceContext
    private EntityManager entityManager;

    @Inject
    private UserService userService;

    @Inject
    private ProjectService projectService;

    @Inject
    private IssueService issueService;

    public boolean hasUserPermissionToIssue(Long userId, Long issueId, String methodName) {
        List<Role> roles = userService.findUser(userId).getUserCredentials().getRoles();
        Issue issue = issueService.findIssue(issueId);

        return checkPermissionForEntity(userId, methodName, roles, issue);
    }

    public boolean hasUserPermissionToUser(Long requesterId, Long userId, String methodName) {
        List<Role> roles = userService.findUser(requesterId).getUserCredentials().getRoles();
        User user = userService.findUser(userId);

        return checkPermissionForEntity(userId, methodName, roles, user);
    }

    public boolean hasUserPermissionToProject(Long userId, Long projectId, String methodName) {
        List<Role> roles = userService.findUser(userId).getUserCredentials().getRoles();
        Project project = projectService.findProject(projectId);

        return checkPermissionForEntity(userId, methodName, roles, project);
    }

    public List<Permission> findPermissions() {
        return entityManager.createNamedQuery("Permission.findAll", Permission.class).getResultList();
    }

    public Permission savePermission(PermissionDTO permissionDTO) {
        Permission permission = new Permission();
        permission.setMethodName(permissionDTO.getMethodName());
        permission.setPermissionLevel(PermissionLevel.valueOf(permissionDTO.getPermissionLevel()));
        permission.setRoleName(permissionDTO.getRoleName());

        entityManager.persist(permission);

        return permission;
    }

    public void removePermission(Long permissionId) {
        Permission permission = entityManager.find(Permission.class, permissionId);
        entityManager.remove(permission);
    }

    public boolean hasUserPermissionToMethod(Long userId, String methodName) {
        List<Role> roles = userService.findUser(userId).getUserCredentials().getRoles();

        return roles.stream().anyMatch(role -> {
            List<Permission> permissionsForMethod = findPermissionByRoleAndMethod(role.getRoleName(), methodName);

            return permissionsForMethod.stream()
                    .anyMatch(permission -> permission.getPermissionLevel() == PermissionLevel.GRANTED);
        });
    }

    private boolean checkPermissionForEntity(Long userId, String methodName, List<Role> roles, Possessable entity) {
        if (entity == null) {
            return false;
        }

        return roles.stream().anyMatch(role -> {
            List<Permission> permissions = findPermissionByRoleAndMethod(role.getRoleName(), methodName);

            return permissions.stream()
                    .anyMatch(permission -> checkPermission(userId, entity, permission));
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

    public List<Permission> findPermissionByRoleAndMethod(String roleName, String methodName) {
        return entityManager.createNamedQuery("Permission.findByRoleAndMethod", Permission.class)
                .setParameter(1, roleName)
                .setParameter(2, methodName)
                .getResultList();
    }
}
