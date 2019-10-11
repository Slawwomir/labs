package rest.resource;

import rest.model.issue.Issue;
import rest.model.issue.IssueStatus;
import rest.model.issue.IssueType;
import rest.model.issue.Issues;
import service.IssueService;
import service.ProjectService;

import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;
import javax.ws.rs.*;
import javax.ws.rs.core.*;
import java.net.URI;
import java.util.ArrayList;
import java.util.List;

@Path("issue")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
@RequestScoped
public class IssueResource {

    @Inject
    private IssueService issueService;

    @Inject
    private ProjectService projectService;

    @GET
    public Response getIssues(@Context UriInfo uriInfo, @QueryParam("start") int start, @QueryParam("size") @DefaultValue("2") int size) {
        List<Issue> projects = issueService.findAllIssues();
        List<Link> links = getLinksForIssues(projects, uriInfo, start, size);
        List<Issue> projectsSubList = projects.subList(start, Math.min(start + size, projects.size()));
        Issues issuesEntity = new Issues(projectsSubList, links);

        return Response.ok(issuesEntity).build();
    }

    @POST
    public Response addIssue(Issue issue) {
        if (issue.getProjectId() == null || projectService.findProject(issue.getProjectId()) == null) {
            return Response.status(Response.Status.BAD_REQUEST).build();
        }

        Issue newIssue = issueService.saveIssue(issue);
        return Response.ok(newIssue).build();
    }

    @PUT
    public Response updateIssue(Issue issue) {
        if (issue == null || issue.getProjectId() == null || issue.getId() == null) {
            return Response.status(Response.Status.BAD_REQUEST).build();
        }

        if (issueService.findIssue(issue.getId()) == null) {
            return Response.status(Response.Status.BAD_REQUEST).build();
        }

        Issue updatedIssue = issueService.saveIssue(issue);

        return Response.ok(updatedIssue).build();
    }

    @DELETE
    @Path("{issueId}")
    public Response removeIssue(@PathParam("issueId") Long issueId) {
        if (issueService.findIssue(issueId) == null) {
            return Response.status(Response.Status.BAD_REQUEST).build();
        }

        issueService.removeIssue(issueId);
        return Response.ok().build();
    }

    @GET
    @Path("{issueId}")
    public Response getIssue(@PathParam("issueId") Long issueId) {
        Issue issue = issueService.findIssue(issueId);

        return issue == null ? Response.status(Response.Status.BAD_REQUEST).build() : Response.ok(issue).build();
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

    private List<Link> getLinksForIssues(List<Issue> projects, UriInfo uriInfo, int start, int size) {
        UriBuilder pathBuilder = uriInfo.getAbsolutePathBuilder();
        pathBuilder.queryParam("start", "{start}");
        pathBuilder.queryParam("size", "{size}");

        List<Link> links = new ArrayList<>();
        links.add(getSelfLink(uriInfo));

        if (start + size < projects.size()) {
            int next = start + size;
            URI nextUri = pathBuilder.clone().build(next, size);
            Link nextLink = Link.fromUri(nextUri)
                    .rel("next")
                    .type("application/json")
                    .build();
            links.add(nextLink);
        }

        if (start > 0) {
            int previous = Math.max(start - size, 0);
            URI previousUri = pathBuilder.clone().build(previous, size);
            Link previousLink = Link.fromUri(previousUri)
                    .rel("previous")
                    .type("application/json")
                    .build();
            links.add(previousLink);
        }

        return links;
    }

    private Link getSelfLink(UriInfo uriInfo) {
        return Link.fromUri(uriInfo.getRequestUri()).rel("self").build();
    }
}
