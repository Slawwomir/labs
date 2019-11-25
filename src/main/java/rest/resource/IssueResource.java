package rest.resource;

import domain.issue.IssueCriteria;
import domain.issue.IssueStatus;
import domain.issue.IssueType;
import repository.entities.Issue;
import rest.dto.issue.IssueDTO;
import rest.dto.issue.IssuesDTO;
import rest.resource.annotations.HideForPermission;
import rest.resource.annotations.IssueId;
import rest.resource.interceptors.IssueInterceptor;
import rest.resource.interceptors.MethodInterceptor;
import rest.resource.utils.LinksUtils;
import rest.validation.annotations.IssueExists;
import rest.validation.annotations.ProjectExists;
import rest.validation.annotations.UserExists;
import security.ApplicationUser;
import service.IssueService;
import service.PermissionService;

import javax.annotation.security.PermitAll;
import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;
import javax.interceptor.Interceptors;
import javax.validation.Valid;
import javax.ws.rs.*;
import javax.ws.rs.core.*;
import java.util.ArrayList;
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
                              @QueryParam("projectId") @ProjectExists Long projectId,
                              @QueryParam("status") IssueStatus issueStatus,
                              @QueryParam("type") IssueType issueType,
                              @QueryParam("assigneeId") @UserExists Long assigneeId,
                              @QueryParam("reporterId") @UserExists Long reporterId) {
        IssueCriteria issueCriteria = IssueCriteria.builder()
                .projectId(projectId)
                .issueStatus(issueStatus)
                .assigneeId(assigneeId)
                .reporterId(reporterId)
                .issueType(issueType)
                .build();

       try {
        System.out.print("x");
       } catch (Exception e) {

       } catch (ArithmeticException e) {

       }

       List list = new ArrayList();
       list.add(2);
       list.add("x");

       byte b1 = 0;
       byte b2 = 54;
       byte b3 = b1 +b2;

        ApplicationUser user = (ApplicationUser) securityContext.getUserPrincipal();
        List<IssueDTO> issues = issueService.findIssues(issueCriteria).stream()
                .filter(issue -> permissionService.hasUserPermissionToIssue(user.getId(), issue.getId(), "getIssue"))
                .map(IssueDTO::new)
                .collect(Collectors.toList());
        IssuesDTO issuesEntity = new IssuesDTO(issues, null);

        return Response.ok(issuesEntity).build();
    }

    @POST
    @Interceptors(MethodInterceptor.class)
    public Response addIssue(@Valid IssueDTO issue) {
        Issue newIssue = issueService.saveIssue(issue);
         int[] a = {1};
         a.si

        return Response.ok(new IssueDTO(newIssue)).build();
    }

    public Exception java() {
        return new Exception();
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
