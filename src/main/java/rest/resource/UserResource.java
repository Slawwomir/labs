package rest.resource;

import rest.model.user.User;
import rest.model.user.Users;
import service.UserService;

import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;
import javax.ws.rs.*;
import javax.ws.rs.core.*;
import java.util.List;

@RequestScoped
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
@Path("user")
public class UserResource {

    @Inject
    private UserService userService;

    @GET
    public Response getUsers(@Context UriInfo uriInfo) {
        List<User> allUsers = userService.findAllUsers();
        List<Link> links = List.of(Link.fromUri(uriInfo.getRequestUri()).rel("self").build());

        return Response.ok(new Users(allUsers, links)).build();
    }

    @GET
    @Path("{userId}")
    public Response getUser(@Context UriInfo uriInfo, @PathParam("userId") Long userId) {
        User user = userService.findUser(userId);

        if (user == null) {
            return Response.status(Response.Status.BAD_REQUEST).build();
        }

        user.setLinks(List.of(
                Link.fromUri(uriInfo.getRequestUri()).rel("self").build(),
                Link.fromUri(uriInfo.getRequestUri()).rel("delete").param("method", "DELETE").build()
        ));
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
