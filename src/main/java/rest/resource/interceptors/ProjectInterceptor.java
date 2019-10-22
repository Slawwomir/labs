package rest.resource.interceptors;

import rest.dto.project.ProjectDTO;
import rest.resource.Secured;
import rest.resource.annotations.ProjectId;
import rest.resource.exceptions.ProjectNotFoundException;
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

public class ProjectInterceptor {

    @Inject
    private PermissionService permissionService;

    @AroundInvoke
    public Object checkPermission(InvocationContext invocationContext) throws Exception {
        Long projectId = getProjectId(invocationContext);
        ApplicationUser applicationUser = getApplicationUser(invocationContext);
        String methodName = cutMethodName(invocationContext.getMethod().getName());

        if (!permissionService.hasUserPermissionToProject(applicationUser.getId(), projectId, methodName)) {
            return Response.status(Response.Status.FORBIDDEN).build();
        }

        return invocationContext.proceed();
    }

    private ApplicationUser getApplicationUser(InvocationContext invocationContext) {
        Secured securedResource = (Secured) invocationContext.getTarget();
        SecurityContext securityContext = securedResource.getSecurityContext();
        return (ApplicationUser) securityContext.getUserPrincipal();
    }

    private Long getProjectId(InvocationContext invocationContext) {
        Optional<Long> projectId = Arrays.stream(invocationContext.getParameters())
                .filter(parameter -> parameter instanceof ProjectDTO)
                .findAny()
                .map(project -> ((ProjectDTO) project).getId());

        if (projectId.isPresent()) {
            return projectId.get();
        }

        Annotation[][] parameterAnnotations = invocationContext.getMethod().getParameterAnnotations();
        OptionalInt index = IntStream.range(0, parameterAnnotations.length)
                .filter(i -> hasProjectIdAnnotation(parameterAnnotations[i]))
                .findAny();

        if (index.isPresent()) {
            return (Long) invocationContext.getParameters()[index.getAsInt()];
        }

        throw new ProjectNotFoundException(
                String.format("Method %s doesn't contain a project or project id in parameters",
                        invocationContext.getMethod().getName())
        );
    }

    private boolean hasProjectIdAnnotation(Annotation[] parameterAnnotation) {
        return Arrays.stream(parameterAnnotation)
                .anyMatch(annotation -> ((Annotation) annotation).annotationType().equals(ProjectId.class));
    }

    private String cutMethodName(String methodName) {
        if (methodName.startsWith("getProject")) {
            return "getProject";
        }

        return methodName;
    }
}
