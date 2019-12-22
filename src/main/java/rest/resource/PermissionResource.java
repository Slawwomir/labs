package rest.resource;

import domain.permission.PermissionLevel;
import repository.entities.Permission;
import rest.dto.permission.PermissionDTO;
import rest.resource.annotations.HideForPermission;
import rest.resource.interceptors.MethodInterceptor;
import security.ApplicationUser;
import security.domain.Role;
import service.PermissionService;

import javax.annotation.security.PermitAll;
import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;
import javax.interceptor.Interceptors;
import javax.ws.rs.*;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.SecurityContext;
import java.lang.reflect.Method;
import java.util.*;
import java.util.stream.Collectors;

import static javax.ws.rs.core.MediaType.APPLICATION_JSON;

@Path("permission")
@Consumes(APPLICATION_JSON)
@Produces(APPLICATION_JSON)
@RequestScoped
public class PermissionResource implements Secured {

    @Inject
    private PermissionService permissionService;

    @Context
    private SecurityContext securityContext;

    @GET
    @Interceptors(MethodInterceptor.class)
    public Response getPermissions() {
        List<PermissionDTO> permissions = permissionService.findPermissions().stream()
                .map(PermissionDTO::new)
                .collect(Collectors.toList());

        return Response.ok(permissions).build();
    }

    @POST
    @Interceptors(MethodInterceptor.class)
    public Response addPermission(PermissionDTO permissionDTO) {
        Permission permission = permissionService.savePermission(permissionDTO);

        return Response.ok(new PermissionDTO(permission)).build();
    }

    @DELETE
    @Interceptors(MethodInterceptor.class)
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
        methodNames.addAll(getMethodNames(PermissionResource.class));

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

    @GET
    @Path("check/{methodName}")
    @HideForPermission
    public Response checkPermission(@PathParam("methodName") String methodName) {
        List<PermissionLevel> permissionLevels = new ArrayList<>();
        ApplicationUser applicationUser = (ApplicationUser) securityContext.getUserPrincipal();

        applicationUser.getRoles().forEach(role -> {
            List<Permission> permissions = permissionService.findPermissionByRoleAndMethod(role, methodName);

            permissions.forEach(permission -> permissionLevels.add(permission.getPermissionLevel()));
        });

        return Response.ok(permissionLevels).build();
    }

    @Override
    @HideForPermission
    public SecurityContext getSecurityContext() {
        return securityContext;
    }

    @HideForPermission
    private Set<String> getMethodNames(Class clazz) {
        return Arrays.stream(clazz.getDeclaredMethods())
                .filter(this::isVisibleForPermission)
                .map(Method::getName)
                .filter(methodName -> !methodName.contains("lambda"))
                .collect(Collectors.toSet());
    }

    @HideForPermission
    private boolean isVisibleForPermission(Method method) {
        return Arrays.stream(method.getDeclaredAnnotations())
                .noneMatch(annotation -> annotation.annotationType().equals(HideForPermission.class) || annotation.annotationType().equals(PermitAll.class));
    }
}
