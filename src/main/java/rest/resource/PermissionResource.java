package rest.resource;

import domain.permission.PermissionLevel;
import repository.entities.Permission;
import rest.dto.permission.PermissionDTO;
import security.domain.Role;
import service.PermissionService;

import javax.annotation.security.PermitAll;
import javax.annotation.security.RolesAllowed;
import javax.inject.Inject;
import javax.ws.rs.*;
import javax.ws.rs.core.Response;

import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import static javax.ws.rs.core.MediaType.APPLICATION_JSON;

@Path("permission")
@Consumes(APPLICATION_JSON)
@Produces(APPLICATION_JSON)
public class PermissionResource {

    @Inject
    private PermissionService permissionService;

    @GET
    @RolesAllowed({"ADMIN"})
    public Response getPermissions() {
        List<PermissionDTO> permissions = permissionService.findPermissions().stream()
                .map(PermissionDTO::new)
                .collect(Collectors.toList());

        return Response.ok(permissions).build();
    }

    @POST
    @RolesAllowed({"ADMIN"})
    public Response addPermission(PermissionDTO permissionDTO) {
        Permission permission = permissionService.savePermission(permissionDTO);

        return Response.ok(new PermissionDTO(permission)).build();
    }

    @DELETE
    @RolesAllowed({"ADMIN"})
    @Path("{permissionId}")
    public Response removePermission(@PathParam("permissionId") Long permissionId) {
        permissionService.removePermission(permissionId);

        return Response.ok().build();
    }

    @GET
    @Path("methods")
    @PermitAll
    public Response getAllMethodNames() {
        Set<String> methodNames = new HashSet<>();
        methodNames.addAll(getMethodNames(IssueResource.class));
        methodNames.addAll(getMethodNames(ProjectResource.class));
        methodNames.addAll(getMethodNames(UserResource.class));

        return Response.ok(methodNames).build();
    }

    @GET
    @Path("roles")
    @PermitAll
    public Response getRoles() {
        return Response.ok(Role.values()).build();
    }

    @GET
    @Path("levels")
    @PermitAll
    public Response getPermissionLevels() {
        return Response.ok(PermissionLevel.values()).build();
    }

    private Set<String> getMethodNames(Class clazz) {
        return Arrays.stream(clazz.getDeclaredMethods())
                .map(Method::getName)
                .filter(methodName -> !methodName.contains("lambda"))
                .collect(Collectors.toSet());
    }
}
