package rest.resource;

import repository.entities.User;
import rest.dto.user.UserDTO;
import rest.dto.user.UsersDTO;
import rest.resource.annotations.HideForPermission;
import rest.resource.annotations.UserId;
import rest.resource.interceptors.MethodInterceptor;
import rest.resource.interceptors.UserInterceptor;
import rest.resource.utils.LinksUtils;
import rest.validation.annotations.UserExists;
import security.ApplicationUser;
import service.PermissionService;
import service.UserService;

import javax.annotation.security.RolesAllowed;
import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;
import javax.interceptor.Interceptors;
import javax.validation.Valid;
import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.DefaultValue;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.*;
import java.util.List;
import java.util.stream.Collectors;

@RequestScoped
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
@Path("users")
public class UserResource implements Secured {

    @Context
    private SecurityContext securityContext;

    @Inject
    private UserService userService;

    @Inject
    private LinksUtils linksUtils;

    @Inject
    private PermissionService permissionService;

    @GET
    @HideForPermission
    public Response getUsers() {
        ApplicationUser applicationUser = (ApplicationUser) securityContext.getUserPrincipal();

        List<UserDTO> allUsers = userService.findAllUsers().stream()
                .filter(user -> permissionService.hasUserPermissionToUser(applicationUser.getId(), user.getId(), "getUser"))
                .map(UserDTO::new)
                .collect(Collectors.toList());

        return Response.ok(new UsersDTO(allUsers, null)).build();
    }

    @GET
    @Path("{userId}")
    @Interceptors(UserInterceptor.class)
    public Response getUser(@Context UriInfo uriInfo,
                            @PathParam("userId") @UserExists @UserId Long userId) {
        User user = userService.findUser(userId);
        UserDTO userDTO = new UserDTO(user);
        linksUtils.setLinksForUser(uriInfo, userDTO);

        return Response.ok(userDTO).build();
    }

    @POST
    @Interceptors(MethodInterceptor.class)
    public Response addUser(@Valid UserDTO user) {
        User newUser = userService.saveUser(user);

        return Response.ok(new UserDTO(newUser)).build();
    }

    @PUT
    @Interceptors(UserInterceptor.class)
    public Response updateUser(@Valid UserDTO user) {
        if (user.getId() == null) {
            return Response.status(Response.Status.BAD_REQUEST).build();
        }
        User updatedUser = userService.saveUser(user);

        return Response.ok(new UserDTO(updatedUser)).build();
    }

    @DELETE
    @Path("{userId}")
    @Interceptors(UserInterceptor.class)
    public Response removeUser(@PathParam("userId") @UserExists @UserId Long userId) {
        userService.removeUser(userId);
        return Response.ok().build();
    }

    @Override
    @HideForPermission
    public SecurityContext getSecurityContext() {
        return securityContext;
    }
}
