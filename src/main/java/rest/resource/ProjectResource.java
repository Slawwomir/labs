package rest.resource;

import rest.model.issue.Issue;
import rest.model.issue.Issues;
import rest.model.project.Project;
import rest.model.project.Projects;
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

@Path("project")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
@RequestScoped
public class ProjectResource {

    @Inject
    private ProjectService projectService;

    @Inject
    private IssueService issueService;

    @GET
    public Response getProjects(
            @QueryParam("start") int start,
            @QueryParam("size") @DefaultValue("2") int size,
            @Context UriInfo uriInfo
    ) {
        if(size <= 0) {
            size = Integer.MAX_VALUE;
        }

        List<Project> projects = projectService.findAllProjects();
        List<Link> links = getLinksForProjects(projects, uriInfo, start, size);
        List<Project> projectsSubList = projects.subList(start, Math.min(start + size, projects.size()));
        projectsSubList.forEach(project -> setLinksForProject(uriInfo, project));
        Projects projectsEntity = new Projects(projectsSubList, links);

        return Response.ok(projectsEntity).links(links.toArray(Link[]::new)).build();
    }

    @POST
    public Response addProject(Project project) {
        if (project.getId() != null && projectService.findProject(project.getId()) != null) {
            return Response.status(Response.Status.BAD_REQUEST).build();
        }

        Project newProject = projectService.saveProject(project);
        return Response.ok(newProject).build();
    }

    @PUT
    public Response updateProject(Project project) {
        if (projectService.findProject(project.getId()) == null) {
            return Response.status(Response.Status.BAD_REQUEST).build();
        }

        Project updatedProject = projectService.saveProject(project);
        return Response.ok(updatedProject).build();
    }

    @DELETE
    @Path("{projectId}")
    public Response removeProject(@PathParam("projectId") Long projectId) {
        projectService.removeProject(projectId);

        return Response.ok().build();
    }

    @GET
    @Path("{projectId}")
    public Response getProject(@Context UriInfo uriInfo, @PathParam("projectId") Long projectId) {
        Project project = projectService.findProject(projectId);

        if (project == null) {
            return Response.status(Response.Status.BAD_REQUEST).build();
        }

        setLinksForProject(uriInfo, project);
        return Response.ok(project).build();
    }

    @GET
    @Path("{projectId}/issues")
    public Response getProjectIssues(@Context UriInfo uriInfo, @PathParam("projectId") Long projectId) {
        Project project = projectService.findProject(projectId);

        if (project == null) {
            return Response.status(Response.Status.BAD_REQUEST).build();
        }

        List<Issue> issues = issueService.findIssuesByProjectId(projectId);
        issues.forEach(issue -> IssueResource.setLinksForIssue(uriInfo, issue));

        List<Link> link = List.of(Link.fromUri(uriInfo.getRequestUri()).rel("self").build());
        return Response.ok(new Issues(issues, link)).build();
    }

    private List<Link> getLinksForProjects(List<Project> projects, UriInfo uriInfo, int start, int size) {
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

    private static void setLinksForProject(@Context UriInfo uriInfo, Project project) {
        Long projectId = project.getId();
        List<Link> links = new ArrayList<>();
        links.add(getSelfLinkForProject(uriInfo, projectId));
        links.add(getDeleteLinkForProject(uriInfo, projectId));
        links.add(getIssuesLinkForProject(uriInfo, projectId));
        links.add(getProjectOwnerForProject(uriInfo, project.getProjectOwnerId()));

        project.setLinks(links);
    }

    private static Link getProjectOwnerForProject(UriInfo uriInfo, Long projectOwnerId) {
        return Link.fromUri(uriInfo.getBaseUriBuilder().path("user").path(String.valueOf(projectOwnerId)).build()).rel("projectOwner").build();
    }

    private static Link getSelfLinkForProject(UriInfo uriInfo, Long projectId) {
        return Link.fromUri(uriInfo.getBaseUriBuilder().path("project").path(String.valueOf(projectId)).build()).rel("self").build();
    }

    private static Link getDeleteLinkForProject(UriInfo uriInfo, Long projectId) {
        return Link.fromUri(uriInfo.getBaseUriBuilder().path("project").path(String.valueOf(projectId)).build()).param("method", "DELETE").rel("delete").build();
    }

    private static Link getIssuesLinkForProject(UriInfo uriInfo, Long projectId) {
        return Link.fromUri(uriInfo.getBaseUriBuilder().path("project").path(String.valueOf(projectId)).path("issues").build()).rel("issues").build();
    }
}
