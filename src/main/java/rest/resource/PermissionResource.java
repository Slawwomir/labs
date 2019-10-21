package rest.resource;

import domain.permission.PermissionLevel;
import security.domain.Role;

import javax.annotation.security.PermitAll;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Response;

import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;

import static javax.ws.rs.core.MediaType.APPLICATION_JSON;

@Path("permission")
@Consumes(APPLICATION_JSON)
@Produces(APPLICATION_JSON)
public class PermissionResource {

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
