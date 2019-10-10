package rest.resource;

import rest.model.project.Project;
import service.ProjectService;

import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;
import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
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

    @GET
    public Response getProjects() {
        List<Project> projects = projectService.findAllProjects();

        return Response.ok(projects).build();
    }

    @POST
    public Response addProject(Project project) {
        projectService.saveProject(project);

        return Response.ok().build();
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

        return Response.ok(project).build();
    }
}
