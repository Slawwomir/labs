package rest.resource.interceptors;

import rest.dto.issue.IssueDTO;
import rest.resource.Secured;
import rest.resource.exceptions.IssueNotFoundException;
import rest.resource.annotations.IssueId;
import security.ApplicationUser;
import service.PermissionService;

import javax.inject.Inject;
import javax.interceptor.AroundInvoke;
import javax.interceptor.InvocationContext;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.SecurityContext;
import java.lang.annotation.Annotation;
import java.util.Arrays;
import java.util.Optional;
import java.util.OptionalInt;
import java.util.stream.IntStream;

public class IssueInterceptor {

    @Inject
    private PermissionService permissionService;

    @AroundInvoke
    public Object checkPermission(InvocationContext invocationContext) throws Exception {
        Long issueId = getIssueId(invocationContext);
        ApplicationUser applicationUser = getApplicationUser(invocationContext);
        String methodName = invocationContext.getMethod().getName();

        if (!permissionService.hasUserPermissionToIssue(applicationUser.getId(), issueId, methodName)) {
            return Response.status(Response.Status.FORBIDDEN).build();
        }

        return invocationContext.proceed();
    }

    private ApplicationUser getApplicationUser(InvocationContext invocationContext) {
        Secured securedResource = (Secured) invocationContext.getTarget();
        SecurityContext securityContext = securedResource.getSecurityContext();
        return (ApplicationUser) securityContext.getUserPrincipal();
    }

    private Long getIssueId(InvocationContext invocationContext) {
        Optional<Long> issueId = Arrays.stream(invocationContext.getParameters())
                .filter(parameter -> parameter instanceof IssueDTO)
                .findAny()
                .map(issue -> ((IssueDTO) issue).getId());

        if (issueId.isPresent()) {
            return issueId.get();
        }

        Annotation[][] parameterAnnotations = invocationContext.getMethod().getParameterAnnotations();
        OptionalInt index = IntStream.range(0, parameterAnnotations.length)
                .filter(i -> hasIssueIdAnnotation(parameterAnnotations[i]))
                .findAny();

        if (index.isPresent()) {
            return (Long) invocationContext.getParameters()[index.getAsInt()];
        }

        throw new IssueNotFoundException(
                String.format("Method %s doesn't contain an issue or issue id in parameters",
                        invocationContext.getMethod().getName())
        );
    }

    private boolean hasIssueIdAnnotation(Annotation[] parameterAnnotation) {
        return Arrays.stream(parameterAnnotation)
                .anyMatch(annotation -> ((Annotation) annotation).annotationType().equals(IssueId.class));
    }
}
