package rest.resource;

import rest.model.issue.Issue;
import rest.model.project.Project;
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
    public Response getProjects() {
        List<Project> projects = projectService.findAllProjects();

        return Response.ok(projects).build();
    }

    @POST
    public Response addProject(Project project) {
        if (projectService.findProject(project.getId()) != null) {
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
    public Response getProject(@PathParam("projectId") Long projectId) {
        Project project = projectService.findProject(projectId);

        return project == null ? Response.status(Response.Status.BAD_REQUEST).build() : Response.ok(project).build();
    }

    @GET
    @Path("{projectId}/issues")
    public Response getProjectIssues(@PathParam("projectId") Long projectId) {
        Project project = projectService.findProject(projectId);

        if (project == null) {
            return Response.status(Response.Status.BAD_REQUEST).build();
        }

        List<Issue> issues = issueService.findIssuesByProjectId(projectId);

        return Response.ok(issues).build();
    }
}
