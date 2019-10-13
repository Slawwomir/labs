package rest.resource;

import repository.entities.Issue;
import rest.dto.issue.IssueDTO;
import domain.issue.IssueStatus;
import domain.issue.IssueType;
import rest.dto.issue.IssuesDTO;
import service.IssueService;
import service.ProjectService;

import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;
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
import javax.ws.rs.core.UriBuilder;
import javax.ws.rs.core.UriInfo;
import java.net.URI;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

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
        List<IssueDTO> projects = issueService.findAllIssues().stream().map(IssueDTO::new).collect(Collectors.toList());
        List<Link> links = getLinksForIssues(projects, uriInfo, start, size);
        List<IssueDTO> projectsSubList = projects.subList(start, Math.min(start + size, projects.size()));
        IssuesDTO issuesEntity = new IssuesDTO(projectsSubList, links);

        return Response.ok(issuesEntity).build();
    }

    @POST
    public Response addIssue(IssueDTO issue) {
        if (issue.getProjectId() == null || projectService.findProject(issue.getProjectId()) == null) {
            return Response.status(Response.Status.BAD_REQUEST).build();
        }

        Issue newIssue = issueService.saveIssue(issue);
        return Response.ok(new IssueDTO(newIssue)).build();
    }

    @PUT
    public Response updateIssue(IssueDTO issue) {
        if (issue == null || issue.getProjectId() == null || issue.getId() == null) {
            return Response.status(Response.Status.BAD_REQUEST).build();
        }

        if (issueService.findIssue(issue.getId()) == null) {
            return Response.status(Response.Status.BAD_REQUEST).build();
        }

        Issue updatedIssue = issueService.saveIssue(issue);

        return Response.ok(new IssueDTO(updatedIssue)).build();
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
    public Response getIssue(@Context UriInfo uriInfo, @PathParam("issueId") Long issueId) {
        Issue issue = issueService.findIssue(issueId);

        if (issue == null) {
            return Response.status(Response.Status.BAD_REQUEST).build();
        }

        IssueDTO issueDTO = new IssueDTO(issue);
        setLinksForIssue(uriInfo, issueDTO);

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

    private List<Link> getLinksForIssues(List<IssueDTO> projects, UriInfo uriInfo, int start, int size) {
        UriBuilder pathBuilder = uriInfo.getAbsolutePathBuilder();
        pathBuilder.queryParam("start", "{start}");
        pathBuilder.queryParam("size", "{size}");

        List<Link> links = new ArrayList<>();
        links.add(Link.fromUri(uriInfo.getRequestUri()).rel("self").build());

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

    public static void setLinksForIssue(@Context UriInfo uriInfo, IssueDTO issue) {
        Long issueId = issue.getId();
        List<Link> links = new ArrayList<>();
        links.add(getSelfLinkForIssue(uriInfo, issueId));
        links.add(getDeleteLinkForIssue(uriInfo, issueId));
        links.add(getProjectLink(uriInfo, issue.getProjectId()));

        if (issue.getReporterId() != null) {
            links.add(getReporterLink(uriInfo, issue.getReporterId()));
        }
        if (issue.getAssigneeId() != null) {
            links.add(getAssigneeLink(uriInfo, issue.getAssigneeId()));
        }

        issue.setLinks(links);
    }

    private static Link getSelfLinkForIssue(UriInfo uriInfo, Long issueId) {
        return Link.fromUri(uriInfo.getBaseUriBuilder().path("issue").path(String.valueOf(issueId)).build()).rel("self").build();
    }

    private static Link getDeleteLinkForIssue(UriInfo uriInfo, Long issueId) {
        return Link.fromUri(uriInfo.getBaseUriBuilder().path("issue").path(String.valueOf(issueId)).build()).param("method", "DELETE").rel("delete").build();
    }

    private static Link getReporterLink(UriInfo uriInfo, Long userId) {
        return Link.fromUri(uriInfo.getBaseUriBuilder().path(UserResource.class).path(UserResource.class, "getUser").build(userId)).rel("reporter").build();
    }

    private static Link getAssigneeLink(UriInfo uriInfo, Long userId) {
        return Link.fromUri(uriInfo.getBaseUriBuilder().path(UserResource.class).path(UserResource.class, "getUser").build(userId)).rel("assignee").build();
    }

    private static Link getProjectLink(UriInfo uriInfo, Long projecId) {
        return Link.fromUri(uriInfo.getBaseUriBuilder().path(ProjectResource.class).path(ProjectResource.class, "getProject").build(projecId)).rel("project").build();
    }
}
