package rest.resource;

import repository.entities.User;
import rest.dto.user.UserDTO;
import rest.dto.user.UsersDTO;
import service.UserService;

import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;
import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.Link;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.UriInfo;
import java.util.List;
import java.util.stream.Collectors;

@RequestScoped
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
@Path("user")
public class UserResource {

    @Inject
    private UserService userService;

    @GET
    public Response getUsers(@Context UriInfo uriInfo) {
        List<UserDTO> allUsers = userService.findAllUsers().stream().map(UserDTO::new).collect(Collectors.toList());
        List<Link> links = List.of(Link.fromUri(uriInfo.getRequestUri()).rel("self").build());

        return Response.ok(new UsersDTO(allUsers, links)).build();
    }

    @GET
    @Path("{userId}")
    public Response getUser(@Context UriInfo uriInfo, @PathParam("userId") Long userId) {
        User user = userService.findUser(userId);

        if (user == null) {
            return Response.status(Response.Status.BAD_REQUEST).build();
        }

        UserDTO userDTO = new UserDTO(user);

        userDTO.setLinks(List.of(
                Link.fromUri(uriInfo.getRequestUri()).rel("self").build(),
                Link.fromUri(uriInfo.getRequestUri()).rel("delete").param("method", "DELETE").build()
        ));
        return Response.ok(userDTO).build();
    }

    @POST
    public Response addUser(UserDTO user) {
        if (user.getId() != null && userService.findUser(user.getId()) != null) {
            return Response.status(Response.Status.BAD_REQUEST).build();
        }

        User newUser = userService.saveUser(user);
        return Response.ok(new UserDTO(newUser)).build();
    }

    @PUT
    public Response updateUser(UserDTO user) {
        if (user == null || user.getId() == null) {
            return Response.status(Response.Status.BAD_REQUEST).build();
        }

        if (userService.findUser(user.getId()) == null) {
            return Response.status(Response.Status.BAD_REQUEST).build();
        }

        User updatedUser = userService.saveUser(user);

        return Response.ok(new UserDTO(updatedUser)).build();
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
