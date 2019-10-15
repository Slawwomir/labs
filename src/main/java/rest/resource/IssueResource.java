package rest.resource;

import repository.entities.Issue;
import rest.dto.issue.IssueDTO;
import domain.issue.IssueStatus;
import domain.issue.IssueType;
import rest.dto.issue.IssuesDTO;
import rest.resource.utils.LinksUtils;
import rest.validation.annotations.IssueExists;
import service.IssueService;

import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;
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
import javax.ws.rs.core.UriInfo;
import java.util.List;
import java.util.stream.Collectors;

@Path("issues")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
@RequestScoped
public class IssueResource {

    @Inject
    private IssueService issueService;

    @Inject
    private LinksUtils linksUtils;

    @GET
    public Response getIssues(@Context UriInfo uriInfo,
                              @QueryParam("start") int start,
                              @QueryParam("size") @DefaultValue("2") int size) {
        List<IssueDTO> issues = issueService.findIssues(start, size).stream()
                .map(IssueDTO::new)
                .collect(Collectors.toList());

        List<Link> links = linksUtils.getLinksForPagination(uriInfo, start, size, issueService.getIssuesCount());
        issues.forEach(issue -> linksUtils.setLinksForIssue(uriInfo, issue));
        IssuesDTO issuesEntity = new IssuesDTO(issues, links);

        return Response.ok(issuesEntity).build();
    }

    @POST
    public Response addIssue(@Valid IssueDTO issue) {
        Issue newIssue = issueService.saveIssue(issue);

        return Response.ok(new IssueDTO(newIssue)).build();
    }

    @PUT
    public Response updateIssue(@Valid IssueDTO issue) {
        if (issue.getId() == null) {
            return Response.status(Response.Status.BAD_REQUEST).build();
        }
        Issue updatedIssue = issueService.saveIssue(issue);

        return Response.ok(new IssueDTO(updatedIssue)).build();
    }

    @DELETE
    @Path("{issueId}")
    public Response removeIssue(@PathParam("issueId") @IssueExists Long issueId) {
        issueService.removeIssue(issueId);

        return Response.ok().build();
    }

    @GET
    @Path("{issueId}")
    public Response getIssue(@Context UriInfo uriInfo,
                             @PathParam("issueId") @IssueExists Long issueId) {
        Issue issue = issueService.findIssue(issueId);
        IssueDTO issueDTO = new IssueDTO(issue);
        linksUtils.setLinksForIssue(uriInfo, issueDTO);

        return Response.ok(issueDTO).build();
    }

    @GET
    @Path("status")
    public Response getStatuses() {
        List<IssueStatus> issueStatuses = issueService.getIssueStatuses();

        return Response.ok(issueStatuses).build();
    }

    @GET
    @Path("type")
    public Response getIssueTypes() {
        List<IssueType> issueTypes = issueService.getIssueTypes();

        return Response.ok(issueTypes).build();
    }
}
