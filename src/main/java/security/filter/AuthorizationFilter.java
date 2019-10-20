package security.filter;

import security.exception.AuthorizationException;

import javax.annotation.Priority;
import javax.annotation.security.PermitAll;
import javax.annotation.security.RolesAllowed;
import javax.enterprise.context.Dependent;
import javax.ws.rs.Priorities;
import javax.ws.rs.container.ContainerRequestContext;
import javax.ws.rs.container.ContainerRequestFilter;
import javax.ws.rs.container.ResourceInfo;
import javax.ws.rs.core.Context;
import javax.ws.rs.ext.Provider;
import java.util.Arrays;

@Provider
@Dependent
@Priority(Priorities.AUTHORIZATION)
public class AuthorizationFilter implements ContainerRequestFilter {

    @Context
    private ResourceInfo resourceInfo;

    @Override
    public void filter(ContainerRequestContext requestContext) {
        if (resourceInfo.getResourceMethod().isAnnotationPresent(PermitAll.class)) {
            return;
        }

        if (!isAuthenticated(requestContext)) {
            throw new AuthorizationException("User is not authenticated");
        }

        if (resourceInfo.getResourceMethod().isAnnotationPresent(RolesAllowed.class)) {
            String[] roles = resourceInfo.getResourceMethod().getAnnotation(RolesAllowed.class).value();

            if (Arrays.stream(roles).noneMatch(role -> requestContext.getSecurityContext().isUserInRole(role))) {
                throw new AuthorizationException("User is not authorized");
            }
        }
    }

    private boolean isAuthenticated(ContainerRequestContext requestContext) {
        return requestContext.getSecurityContext().getUserPrincipal() != null;
    }
}
