package rest.resource.interceptors;

import rest.resource.Secured;
import security.ApplicationUser;
import service.PermissionService;

import javax.inject.Inject;
import javax.interceptor.AroundInvoke;
import javax.interceptor.InvocationContext;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.SecurityContext;

public class MethodInterceptor {

    @Inject
    private PermissionService permissionService;

    @AroundInvoke
    public Object checkPermission(InvocationContext invocationContext) throws Exception {
        ApplicationUser applicationUser = getApplicationUser(invocationContext);
        String methodName = invocationContext.getMethod().getName();

        if (!permissionService.hasUserPermissionToMethod(applicationUser.getId(), methodName)) {
            return Response.status(Response.Status.FORBIDDEN).build();
        }

        return invocationContext.proceed();
    }

    private ApplicationUser getApplicationUser(InvocationContext invocationContext) {
        Secured securedResource = (Secured) invocationContext.getTarget();
        SecurityContext securityContext = securedResource.getSecurityContext();
        return (ApplicationUser) securityContext.getUserPrincipal();
    }
}
