package rest.resource.interceptors;

import rest.dto.user.UserDTO;
import rest.resource.Secured;
import rest.resource.exceptions.UserNotFoundException;
import rest.resource.annotations.UserId;
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

public class UserInterceptor {

    @Inject
    private PermissionService permissionService;

    @AroundInvoke
    public Object checkPermission(InvocationContext invocationContext) throws Exception {
        Long userId = getUserId(invocationContext);
        ApplicationUser applicationUser = getApplicationUser(invocationContext);
        String methodName = invocationContext.getMethod().getName();

        if (!permissionService.hasUserPermissionToUser(applicationUser.getId(), userId, methodName)) {
            return Response.status(Response.Status.FORBIDDEN).build();
        }

        return invocationContext.proceed();
    }

    private ApplicationUser getApplicationUser(InvocationContext invocationContext) {
        Secured securedResource = (Secured) invocationContext.getTarget();
        SecurityContext securityContext = securedResource.getSecurityContext();
        return (ApplicationUser) securityContext.getUserPrincipal();
    }

    private Long getUserId(InvocationContext invocationContext) {
        Optional<Long> userId = Arrays.stream(invocationContext.getParameters())
                .filter(parameter -> parameter instanceof UserDTO)
                .findAny()
                .map(user -> ((UserDTO) user).getId());

        if (userId.isPresent()) {
            return userId.get();
        }

        Annotation[][] parameterAnnotations = invocationContext.getMethod().getParameterAnnotations();
        OptionalInt index = IntStream.range(0, parameterAnnotations.length)
                .filter(i -> hasUserIdAnnotation(parameterAnnotations[i]))
                .findAny();

        if (index.isPresent()) {
            return (Long) invocationContext.getParameters()[index.getAsInt()];
        }

        throw new UserNotFoundException(
                String.format("Method %s doesn't contain an user or user id in parameters",
                        invocationContext.getMethod().getName())
        );
    }

    private boolean hasUserIdAnnotation(Annotation[] parameterAnnotation) {
        return Arrays.stream(parameterAnnotation)
                .anyMatch(annotation -> ((Annotation) annotation).annotationType().equals(UserId.class));
    }
}
