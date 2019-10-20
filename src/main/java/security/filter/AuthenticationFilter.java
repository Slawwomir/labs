package security.filter;

import org.apache.commons.lang3.StringUtils;
import repository.entities.Role;
import repository.entities.UserCredentials;
import security.ApplicationUser;
import security.DefaultSecurityContext;
import security.model.TokenDetails;
import security.service.TokenService;
import service.UserService;

import javax.annotation.Priority;
import javax.enterprise.context.Dependent;
import javax.inject.Inject;
import javax.ws.rs.Priorities;
import javax.ws.rs.container.ContainerRequestContext;
import javax.ws.rs.container.ContainerRequestFilter;
import javax.ws.rs.core.HttpHeaders;
import javax.ws.rs.ext.Provider;
import java.util.Date;
import java.util.Set;
import java.util.stream.Collectors;

@Provider
@Dependent
@Priority(Priorities.AUTHENTICATION)
public class AuthenticationFilter implements ContainerRequestFilter {

    @Inject
    private UserService userService;

    @Inject
    private TokenService tokenService;

    @Override
    public void filter(ContainerRequestContext requestContext) {
        String authHeader = requestContext.getHeaderString(HttpHeaders.AUTHORIZATION);

        if (StringUtils.isEmpty(authHeader)) {
            return;
        }

        String[] header = authHeader.split(" ");

        if (header[0].equals("Bearer")) {
            setSecurityContext(requestContext, header[1]);
        }
    }

    private void setSecurityContext(ContainerRequestContext requestContext, String token) {
        TokenDetails tokenDetails = tokenService.parseToken(token);
        UserCredentials userCredentials = userService.findUserCredentials(Long.valueOf(tokenDetails.getId()));

        if (!isUserPasswordLive(tokenDetails, userCredentials)) {
            return;
        }

        ApplicationUser applicationUser = getApplicationUser(userCredentials);
        requestContext.setSecurityContext(new DefaultSecurityContext(applicationUser));
    }

    private ApplicationUser getApplicationUser(UserCredentials userCredentials) {
        return new ApplicationUser(userCredentials.getUser().getId(), userCredentials.getUser().getName(), getRoles(userCredentials));
    }

    private Set<String> getRoles(UserCredentials userCredentials) {
        return userCredentials.getRoles()
                .stream()
                .map(Role::getRoleName)
                .collect(Collectors.toSet());
    }

    private boolean isUserPasswordLive(TokenDetails tokenDetails, UserCredentials userCredentials) {
        Date passwordChangedDate = userCredentials.getPasswordChangedDate();

        if (passwordChangedDate == null) {
            return true;
        }

        return tokenDetails.getIssuedTime().toInstant().isBefore(passwordChangedDate.toInstant());
    }
}
