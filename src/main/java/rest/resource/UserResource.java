package rest.resource;

import repository.entities.User;
import rest.dto.user.UserDTO;
import rest.dto.user.UsersDTO;
import rest.resource.utils.LinksUtils;
import rest.validation.annotations.UserExists;
import service.UserService;

import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;
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
@Path("users")
public class UserResource {

    @Inject
    private UserService userService;

    @Inject
    private LinksUtils linksUtils;

    @GET
    public Response getUsers(
            @Context UriInfo uriInfo,
            @QueryParam("start") int start,
            @QueryParam("size") @DefaultValue("2") int size) {
        List<UserDTO> allUsers = userService.findUsers(start, size).stream()
                .map(UserDTO::new)
                .collect(Collectors.toList());

        allUsers.forEach(user -> linksUtils.setLinksForUser(uriInfo, user));
        List<Link> links = linksUtils.getLinksForPagination(uriInfo, start, size, userService.getUsersCount());

        return Response.ok(new UsersDTO(allUsers, links)).build();
    }

    @GET
    @Path("{userId}")
    public Response getUser(@Context UriInfo uriInfo,
                            @PathParam("userId") @UserExists Long userId) {
        User user = userService.findUser(userId);
        UserDTO userDTO = new UserDTO(user);
        linksUtils.setLinksForUser(uriInfo, userDTO);

        return Response.ok(userDTO).build();
    }

    @POST
    public Response addUser(@Valid UserDTO user) {
        User newUser = userService.saveUser(user);

        return Response.ok(new UserDTO(newUser)).build();
    }

    @PUT
    public Response updateUser(@Valid UserDTO user) {
        if (user.getId() == null) {
            return Response.status(Response.Status.BAD_REQUEST).build();
        }
        User updatedUser = userService.saveUser(user);

        return Response.ok(new UserDTO(updatedUser)).build();
    }

    @DELETE
    @Path("{userId}")
    public Response removeUser(@PathParam("userId") @UserExists Long userId) {
        userService.removeUser(userId);
        return Response.ok().build();
    }
}
