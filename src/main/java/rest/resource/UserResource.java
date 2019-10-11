package rest.resource;

import rest.model.user.User;
import service.UserService;

import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.core.Response;

@RequestScoped
@Path("user")
public class UserResource {

    private final UserService userService;

    @Inject
    public UserResource(UserService userService) {
        this.userService = userService;
    }

    @GET
    public Response getUsers() {
        return Response.ok(userService.findAllUsers()).build();
    }

    @GET
    @Path("{userId}")
    public Response getUser(@PathParam("userId") Long userId) {
        User user = userService.findUser(userId);

        if (user == null) {
            return Response.status(Response.Status.BAD_REQUEST).build();
        }

        return Response.ok(user).build();
    }

    @POST
    public Response addUser(User user) {
        if (user.getId() != null && userService.findUser(user.getId()) != null) {
            return Response.status(Response.Status.BAD_REQUEST).build();
        }

        User newUser = userService.saveUser(user);
        return Response.ok(newUser).build();
    }

    @PUT
    public Response updateUser(User user) {
        if (user == null || user.getId() == null) {
            return Response.status(Response.Status.BAD_REQUEST).build();
        }

        if (userService.findUser(user.getId()) == null) {
            return Response.status(Response.Status.BAD_REQUEST).build();
        }

        User updatedUser = userService.saveUser(user);

        return Response.ok(updatedUser).build();
    }

    @DELETE
    @Path("{userId}")
    public Response removeUser(@PathParam("userId") Long userId) {
        if (userService.findUser(userId) == null) {
            return Response.status(Response.Status.BAD_REQUEST).build();
        }

        userService.removeUser(userId);
        return Response.ok().build();
    }
}
