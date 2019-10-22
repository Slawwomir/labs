package rest.resource;

import repository.entities.Issue;
import rest.dto.issue.IssueDTO;
import domain.issue.IssueStatus;
import domain.issue.IssueType;
import rest.dto.issue.IssuesDTO;
import rest.resource.annotations.HideForPermission;
import rest.resource.interceptors.IssueInterceptor;
import rest.resource.interceptors.MethodInterceptor;
import rest.resource.utils.LinksUtils;
import rest.validation.annotations.IssueExists;
import rest.resource.annotations.IssueId;
import security.ApplicationUser;
import service.IssueService;
import service.PermissionService;

import javax.annotation.security.PermitAll;
import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;
import javax.interceptor.Interceptors;
import javax.validation.Valid;
import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.DefaultValue;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.Link;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.SecurityContext;
import javax.ws.rs.core.UriInfo;
import java.util.List;
import java.util.stream.Collectors;

@Path("issues")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
@RequestScoped
public class IssueResource implements Secured {

    @Context
    private SecurityContext securityContext;

    @Inject
    private IssueService issueService;

    @Inject
    private LinksUtils linksUtils;

    @Inject
    private PermissionService permissionService;

    @GET
    @HideForPermission
    public Response getIssues(@Context UriInfo uriInfo,
                              @QueryParam("start") int start,
                              @QueryParam("size") @DefaultValue("2") int size) {
        ApplicationUser user = (ApplicationUser) securityContext.getUserPrincipal();

        List<IssueDTO> issues = issueService.findIssues(start, size).stream()
                .filter(issue -> permissionService.hasUserPermissionToIssue(user.getId(), issue.getId(), "getIssue"))
                .map(IssueDTO::new)
                .collect(Collectors.toList());

        List<Link> links = linksUtils.getLinksForPagination(uriInfo, start, size, issueService.getIssuesCount());
        issues.forEach(issue -> linksUtils.setLinksForIssue(uriInfo, issue));
        IssuesDTO issuesEntity = new IssuesDTO(issues, links);

        return Response.ok(issuesEntity).build();
    }

    @POST
    @Interceptors(MethodInterceptor.class)
    public Response addIssue(@Valid IssueDTO issue) {
        Issue newIssue = issueService.saveIssue(issue);

        return Response.ok(new IssueDTO(newIssue)).build();
    }

    @PUT
    @Interceptors(IssueInterceptor.class)
    public Response updateIssue(@Valid IssueDTO issue) {
        if (issue.getId() == null) {
            return Response.status(Response.Status.BAD_REQUEST).build();
        }
        Issue updatedIssue = issueService.saveIssue(issue);

        return Response.ok(new IssueDTO(updatedIssue)).build();
    }

    @DELETE
    @Path("{issueId}")
    @Interceptors(IssueInterceptor.class)
    public Response removeIssue(@PathParam("issueId") @IssueExists @IssueId Long issueId) {
        issueService.removeIssue(issueId);

        return Response.ok().build();
    }

    @GET
    @Path("{issueId}")
    @Interceptors(IssueInterceptor.class)
    public Response getIssue(@Context UriInfo uriInfo,
                             @PathParam("issueId") @IssueExists @IssueId Long issueId) {
        Issue issue = issueService.findIssue(issueId);
        IssueDTO issueDTO = new IssueDTO(issue);
        linksUtils.setLinksForIssue(uriInfo, issueDTO);

        return Response.ok(issueDTO).build();
    }

    @GET
    @Path("status")
    @PermitAll
    public Response getStatuses() {
        List<IssueStatus> issueStatuses = issueService.getIssueStatuses();

        return Response.ok(issueStatuses).build();
    }

    @GET
    @Path("type")
    @PermitAll
    public Response getIssueTypes() {
        List<IssueType> issueTypes = issueService.getIssueTypes();

        return Response.ok(issueTypes).build();
    }

    @Override
    @HideForPermission
    public SecurityContext getSecurityContext() {
        return securityContext;
    }
}
