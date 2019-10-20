package security;

import security.domain.Constants;

import javax.ws.rs.core.SecurityContext;
import java.security.Principal;

public class DefaultSecurityContext implements SecurityContext {

    private final ApplicationUser applicationUser;

    public DefaultSecurityContext(ApplicationUser applicationUser) {
        this.applicationUser = applicationUser;
    }

    @Override
    public Principal getUserPrincipal() {
        return applicationUser;
    }

    @Override
    public boolean isUserInRole(String role) {
        return applicationUser.getRoles().contains(role);
    }

    @Override
    public boolean isSecure() {
        return false;
    }

    @Override
    public String getAuthenticationScheme() {
        return Constants.BEARER;
    }
}
