package rest.dto.permission;

import lombok.Data;
import lombok.NoArgsConstructor;
import repository.entities.Permission;

@Data
@NoArgsConstructor
public class PermissionDTO {
    private Long id;
    private String methodName;
    private String permissionLevel;
    private String roleName;

    public PermissionDTO(Permission permission) {
        this.id = permission.getId();
        this.methodName = permission.getMethodName();
        this.permissionLevel = permission.getPermissionLevel().name();
        this.roleName = permission.getRoleName();
    }
}
