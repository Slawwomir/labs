package rest.resource.interceptors;

import rest.dto.issue.IssueDTO;
import rest.resource.IssueResource;
import security.ApplicationUser;
import service.PermissionService;

import javax.inject.Inject;
import javax.interceptor.AroundInvoke;
import javax.interceptor.InvocationContext;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.SecurityContext;

public class IssueInterceptor {

    @Inject
    private PermissionService permissionService;

    @AroundInvoke
    public Object checkPermission(InvocationContext invocationContext) throws Exception {
        IssueDTO issueDTO = (IssueDTO) invocationContext.getParameters()[0];
        IssueResource issueResource = (IssueResource) invocationContext.getTarget();
        SecurityContext securityContext = issueResource.getSecurityContext();
        ApplicationUser applicationUser = (ApplicationUser) securityContext.getUserPrincipal();
        String methodName = invocationContext.getMethod().getName();

        if (permissionService.hasUserPermissionToIssue(applicationUser.getId(), issueDTO.getId(), methodName)) {
            return Response.status(Response.Status.FORBIDDEN).build();
        }

        return invocationContext.proceed();
    }
}
