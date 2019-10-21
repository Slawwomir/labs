package security.resource;

import repository.entities.User;
import security.ApplicationUser;
import security.domain.Role;
import security.model.UserCredentials;
import security.resource.dto.PasswordDTO;
import security.resource.dto.TokenDTO;
import security.service.AuthenticationService;
import security.service.TokenService;
import service.UserService;

import javax.annotation.security.PermitAll;
import javax.inject.Inject;
import javax.ws.rs.*;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.SecurityContext;

import java.util.List;

import static javax.ws.rs.core.MediaType.APPLICATION_JSON;

@Consumes(APPLICATION_JSON)
@Produces(APPLICATION_JSON)
@Path("authentication")
public class AuthenticationResource {

    @Context
    private SecurityContext securityContext;

    @Inject
    private TokenService tokenService;

    @Inject
    private UserService userService;

    @Inject
    private AuthenticationService authenticationService;

    @POST
    @PermitAll
    public Response authenticate(UserCredentials userCredentials) {
        User user = authenticationService.validateCredentials(userCredentials);

        if (user == null) {
            return Response.status(Response.Status.BAD_REQUEST).build();
        }

        String token = tokenService.createTokenForUser(user);
        return Response.ok(
                new TokenDTO(token, user.getName(), user.getId(), List.copyOf(tokenService.getRoles(user)))
        ).build();
    }

    @PUT
    @Path("password")
    public Response changePassword(PasswordDTO passwordDTO) {
        ApplicationUser applicationUser = (ApplicationUser) securityContext.getUserPrincipal();

        if (applicationUser == null) {
            return Response.status(Response.Status.FORBIDDEN).build();
        }

        User user = authenticationService.changePassword(applicationUser.getId(), passwordDTO.getPassword());
        String token = tokenService.createTokenForUser(user);
        return Response.ok(
                new TokenDTO(token, user.getName(), user.getId(), List.copyOf(tokenService.getRoles(user)))
        ).build();
    }

    @GET
    @Path("roles")
    @PermitAll
    public Response getRoles() {
        return Response.ok(Role.values()).build();
    }

    @POST
    @Path("account")
    @PermitAll
    public Response createAccount(UserCredentials userCredentials) {
        User userByName = userService.findUserByName(userCredentials.getUsername());

        if (userByName != null) {
            return Response.status(Response.Status.BAD_REQUEST).entity("This username is not available").build();
        }

        User user = userService.createUser(userCredentials);

        return Response.ok(user).build();
    }
}
