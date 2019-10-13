package rest.resource;

import domain.issue.IssueStatus;
import repository.entities.Issue;
import repository.entities.Project;
import rest.dto.issue.IssueDTO;
import rest.dto.issue.IssuesDTO;
import rest.dto.project.ProjectDTO;
import rest.dto.project.ProjectsDTO;
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
        if (size <= 0) {
            size = Integer.MAX_VALUE;
        }

        List<ProjectDTO> projects = projectService.findAllProjects().stream().map(ProjectDTO::new).collect(Collectors.toList());
        List<Link> links = getLinksForProjects(projects, uriInfo, start, size);
        List<ProjectDTO> projectsSubList = projects.subList(start, Math.min(start + size, projects.size()));
        projectsSubList.forEach(project -> setLinksForProject(uriInfo, project));
        ProjectsDTO projectsEntity = new ProjectsDTO(projectsSubList, links);

        return Response.ok(projectsEntity).links(links.toArray(Link[]::new)).build();
    }

    @POST
    public Response addProject(ProjectDTO project) {
        if (project.getId() != null && projectService.findProject(project.getId()) != null) {
            return Response.status(Response.Status.BAD_REQUEST).build();
        }

        Project newProject = projectService.saveProject(project);
        return Response.ok(new ProjectDTO(newProject)).build();
    }

    @PUT
    public Response updateProject(ProjectDTO project) {
        if (projectService.findProject(project.getId()) == null) {
            return Response.status(Response.Status.BAD_REQUEST).build();
        }

        Project updatedProject = projectService.saveProject(project);
        return Response.ok(new ProjectDTO(updatedProject)).build();
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

        ProjectDTO projectDTO = new ProjectDTO(project);
        setLinksForProject(uriInfo, projectDTO);
        return Response.ok(projectDTO).build();
    }

    @GET
    @Path("{projectId}/issues")
    public Response getProjectIssues(@Context UriInfo uriInfo, @PathParam("projectId") Long projectId, @QueryParam("status") IssueStatus status) {
        Project project = projectService.findProject(projectId);

        if (project == null) {
            return Response.status(Response.Status.BAD_REQUEST).build();
        }
        List<Issue> results;
        if (status != null) {
            results = issueService.findIssuesByProjectIdAndStatus(projectId, status);
        } else {
            results = issueService.findIssuesByProjectId(projectId);
        }

        List<IssueDTO> issues = results.stream().map(IssueDTO::new).collect(Collectors.toList());
        issues.forEach(issue -> IssueResource.setLinksForIssue(uriInfo, issue));

        List<Link> link = List.of(Link.fromUri(uriInfo.getRequestUri()).rel("self").build());
        return Response.ok(new IssuesDTO(issues, link)).build();
    }

    private List<Link> getLinksForProjects(List<ProjectDTO> projects, UriInfo uriInfo, int start, int size) {
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

    private static void setLinksForProject(@Context UriInfo uriInfo, ProjectDTO project) {
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
