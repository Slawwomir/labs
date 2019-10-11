package rest.resource;

import rest.model.issue.Issue;
import rest.model.issue.IssueStatus;
import rest.model.issue.IssueType;
import service.IssueService;
import service.ProjectService;

import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;
import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
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
    public Response getIssues() {
        List<Issue> issues = issueService.findAllIssues();

        return Response.ok(issues).build();
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
}
