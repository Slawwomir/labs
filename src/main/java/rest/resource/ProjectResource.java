package rest.resource;

import domain.issue.IssueStatus;
import repository.entities.Issue;
import repository.entities.Project;
import rest.dto.issue.IssueDTO;
import rest.dto.issue.IssuesDTO;
import rest.dto.project.ProjectDTO;
import rest.dto.project.ProjectsDTO;
import rest.resource.annotations.HideForPermission;
import rest.resource.annotations.IssueId;
import rest.resource.annotations.ProjectId;
import rest.resource.interceptors.IssueInterceptor;
import rest.resource.interceptors.MethodInterceptor;
import rest.resource.interceptors.ProjectInterceptor;
import rest.resource.utils.LinksUtils;
import rest.validation.annotations.ProjectExists;
import security.ApplicationUser;
import service.IssueService;
import service.PermissionService;
import service.ProjectService;

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
import javax.ws.rs.core.*;
import java.util.List;
import java.util.stream.Collectors;

@Path("projects")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
@RequestScoped
public class ProjectResource implements Secured {

    @Context
    private SecurityContext securityContext;

    @Inject
    private ProjectService projectService;

    @Inject
    private IssueService issueService;

    @Inject
    private PermissionService permissionService;

    @Inject
    private LinksUtils linksUtils;

    @GET
    @HideForPermission
    public Response getProjects(
            @QueryParam("start") int start,
            @QueryParam("size") @DefaultValue("2") int size,
            @Context UriInfo uriInfo) {
        if (size <= 0) {
            size = Integer.MAX_VALUE;
        }

        ApplicationUser applicationUser = (ApplicationUser) securityContext.getUserPrincipal();

        List<ProjectDTO> projects = projectService.findProjects(start, size).stream()
                .filter(project -> permissionService.hasUserPermissionToProject(applicationUser.getId(), project.getId(), "getProject"))
                .map(ProjectDTO::new)
                .collect(Collectors.toList());

        List<Link> links = linksUtils.getLinksForPagination(uriInfo, start, size, projectService.getProjectsCount());
        projects.forEach(project -> linksUtils.setLinksForProject(uriInfo, project));
        ProjectsDTO projectsEntity = new ProjectsDTO(projects, links);

        return Response.ok(projectsEntity).links(links.toArray(Link[]::new)).build();
    }

    @POST
    @Interceptors(MethodInterceptor.class)
    public Response addProject(@Valid ProjectDTO project) {
        if (project.getId() != null && projectService.findProject(project.getId()) != null) {
            return Response.status(Response.Status.BAD_REQUEST).build();
        }

        Project newProject = projectService.saveProject(project);
        return Response.ok(new ProjectDTO(newProject)).build();
    }

    @PUT
    @Interceptors(ProjectInterceptor.class)
    public Response updateProject(@Valid ProjectDTO project) {
        if (project.getId() == null) {
            return Response.status(Response.Status.BAD_REQUEST).build();
        }

        Project updatedProject = projectService.saveProject(project);
        return Response.ok(new ProjectDTO(updatedProject)).build();
    }

    @DELETE
    @Path("{projectId}")
    @Interceptors(ProjectInterceptor.class)
    public Response removeProject(@PathParam("projectId") @ProjectExists Long projectId) {
        projectService.removeProject(projectId);

        return Response.ok().build();
    }

    @GET
    @Path("{projectId}")
    @Interceptors(ProjectInterceptor.class)
    public Response getProject(@Context UriInfo uriInfo,
                               @PathParam("projectId") @ProjectExists @ProjectId Long projectId) {
        Project project = projectService.findProject(projectId);
        ProjectDTO projectDTO = new ProjectDTO(project);
        linksUtils.setLinksForProject(uriInfo, projectDTO);
        return Response.ok(projectDTO).build();
    }

    @GET
    @Path("{projectId}/issues")
    @HideForPermission
    public Response getProjectIssues(
            @Context UriInfo uriInfo,
            @PathParam("projectId") @ProjectExists @ProjectId Long projectId,
            @QueryParam("status") IssueStatus status) {
        List<Issue> results;
        if (status != null) {
            results = issueService.findIssuesByProjectIdAndStatus(projectId, status);
        } else {
            results = issueService.findIssuesByProjectId(projectId);
        }

        ApplicationUser user = (ApplicationUser) securityContext.getUserPrincipal();

        List<IssueDTO> issues = results.stream()
                .filter(issue -> permissionService.hasUserPermissionToIssue(user.getId(), issue.getId(), "getIssue"))
                .map(IssueDTO::new)
                .collect(Collectors.toList());
        issues.forEach(issue -> linksUtils.setLinksForIssue(uriInfo, issue));

        List<Link> link = List.of(linksUtils.getSelfLink(uriInfo.getRequestUri(), "self", "GET"));
        return Response.ok(new IssuesDTO(issues, link)).build();
    }

    @GET
    @Path("{projectId}/issues/{issueId}")
    @Interceptors(IssueInterceptor.class)
    public Response getProjectIssue(
            @Context UriInfo uriInfo,
            @PathParam("projectId") @ProjectId Long projectId,
            @PathParam("issueId") @IssueId Long issueId
    ) {
        Project project = projectService.findProject(projectId);

        if (project == null) {
            return Response.status(Response.Status.BAD_REQUEST).build();
        }

        Issue issue = projectService.findIssueFromProject(projectId, issueId);

        if (issue == null) {
            return Response.status(Response.Status.NOT_FOUND).build();
        }

        IssueDTO issueDTO = new IssueDTO(issue);
        linksUtils.setLinksForIssue(uriInfo, issueDTO);

        return Response.ok(issueDTO).build();
    }

    @Override
    @HideForPermission
    public SecurityContext getSecurityContext() {
        return securityContext;
    }
}
