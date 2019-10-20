package security.resource;

import repository.entities.User;
import security.model.UserCredentials;
import security.resource.dto.TokenDTO;
import security.service.AuthenticationService;
import security.service.TokenService;

import javax.annotation.security.PermitAll;
import javax.inject.Inject;
import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.SecurityContext;

import java.util.List;

import static javax.ws.rs.core.MediaType.APPLICATION_JSON;

@Consumes(APPLICATION_JSON)
@Produces(APPLICATION_JSON)
@Path("authentication")
public class AuthenticationResource {

    @Inject
    private TokenService tokenService;

    @Inject
    private AuthenticationService authenticationService;

    @POST
    @PermitAll
    public Response authenticate(UserCredentials userCredentials) {
        User user = authenticationService.validateCredentials(userCredentials);
        String token = tokenService.createTokenForUser(user);
        return Response.ok(
                new TokenDTO(token, List.copyOf(tokenService.getRoles(user)))
        ).build();
    }

}
