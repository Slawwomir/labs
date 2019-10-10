package rest.resource;

import rest.model.issue.Issue;
import service.IssueService;

import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;
import javax.ws.rs.*;
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

    @GET
    public Response getIssues() {
        List<Issue> issues = issueService.findAllIssues();

        return Response.ok(issues).build();
    }

    @POST
    public Response addIssue(Issue issue) {
        issueService.saveIssue(issue);

        return Response.ok().build();
    }

    @DELETE
    @Path("{issueId}")
    public Response removeIssue(@PathParam("issueId") Long issueId) {
        issueService.removeIssue(issueId);

        return Response.ok().build();
    }

    @GET
    @Path("{issueId}")
    public Response getIssue(@PathParam("issueId") Long issueId) {
        Issue issue = issueService.findIssue(issueId);

        return Response.ok(issue).build();
    }
}
