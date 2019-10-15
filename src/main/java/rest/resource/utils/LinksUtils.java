package rest.resource.utils;

import rest.dto.issue.IssueDTO;
import rest.dto.project.ProjectDTO;
import rest.dto.user.UserDTO;
import rest.resource.ProjectResource;
import rest.resource.UserResource;

import javax.enterprise.context.RequestScoped;
import javax.ws.rs.core.Link;
import javax.ws.rs.core.UriBuilder;
import javax.ws.rs.core.UriInfo;
import java.net.URI;
import java.util.ArrayList;
import java.util.List;

@RequestScoped
public class LinksUtils {
    public List<Link> getLinksForPagination(UriInfo uriInfo, int start, int sizeOfPage, Long maxSize) {
        UriBuilder pathBuilder = uriInfo.getAbsolutePathBuilder();
        pathBuilder.queryParam("start", "{start}");
        pathBuilder.queryParam("size", "{size}");

        List<Link> links = new ArrayList<>();
        links.add(getSelfLink(uriInfo.getRequestUri(), "self", "GET"));

        if (start + sizeOfPage < maxSize) {
            int next = start + sizeOfPage;
            URI nextUri = pathBuilder.clone().build(next, sizeOfPage);
            Link nextLink = getSelfLink(nextUri, "next", "GET");
            links.add(nextLink);
        }

        if (start > 0) {
            int previous = Math.max(start - sizeOfPage, 0);
            URI previousUri = pathBuilder.clone().build(previous, sizeOfPage);
            Link previousLink = getSelfLink(previousUri, "previous", "GET");
            links.add(previousLink);
        }

        return links;
    }

    public Link getSelfLink(URI requestUri, String relation, String method) {
        return Link.fromUri(requestUri)
                .rel(relation)
                .param("method", method)
                .build();
    }

    public void setLinksForIssue(UriInfo uriInfo, IssueDTO issue) {
        Long issueId = issue.getId();
        List<Link> links = new ArrayList<>();
        links.add(getSelfLinkForIssue(uriInfo, issueId));
        links.add(getDeleteLinkForIssue(uriInfo, issueId));
        links.add(getProjectLink(uriInfo, issue.getProjectId()));
        links.add(getReporterLink(uriInfo, issue.getReporterId()));

        if (issue.getAssigneeId() != null) {
            links.add(getAssigneeLink(uriInfo, issue.getAssigneeId()));
        }

        issue.setLinks(links);
    }

    public void setLinksForProject(UriInfo uriInfo, ProjectDTO project) {
        Long projectId = project.getId();
        List<Link> links = new ArrayList<>();
        links.add(getSelfLinkForProject(uriInfo, projectId));
        links.add(getDeleteLinkForProject(uriInfo, projectId));
        links.add(getIssuesLinkForProject(uriInfo, projectId));
        links.add(getProjectOwnerForProject(uriInfo, project.getProjectOwnerId()));

        project.setLinks(links);
    }

    public void setLinksForUser(UriInfo uriInfo, UserDTO userDTO) {
        userDTO.setLinks(List.of(
                getSelfLink(uriInfo.getRequestUri(), "self", "GET"),
                getSelfLink(uriInfo.getRequestUri(), "delete", "DELETE")
        ));
    }

    private Link getSelfLinkForIssue(UriInfo uriInfo, Long issueId) {
        return Link.fromUri(
                uriInfo.getBaseUriBuilder()
                        .path("issue")
                        .path(String.valueOf(issueId))
                        .build())
                .rel("self")
                .param("method", "GET")
                .build();
    }

    private Link getDeleteLinkForIssue(UriInfo uriInfo, Long issueId) {
        return Link.fromUri(
                uriInfo.getBaseUriBuilder()
                        .path("issue")
                        .path(String.valueOf(issueId))
                        .build())
                .rel("delete")
                .param("method", "DELETE")
                .build();
    }

    private Link getReporterLink(UriInfo uriInfo, Long userId) {
        return Link.fromUri(
                uriInfo.getBaseUriBuilder()
                        .path(UserResource.class)
                        .path(UserResource.class, "getUser")
                        .build(userId))
                .rel("reporter")
                .param("method", "GET")
                .build();
    }

    private Link getAssigneeLink(UriInfo uriInfo, Long userId) {
        return Link.fromUri(
                uriInfo.getBaseUriBuilder()
                        .path(UserResource.class)
                        .path(UserResource.class, "getUser")
                        .build(userId))
                .rel("assignee")
                .param("method", "GET")
                .build();
    }

    private Link getProjectLink(UriInfo uriInfo, Long projecId) {
        return Link.fromUri(
                uriInfo.getBaseUriBuilder()
                        .path(ProjectResource.class)
                        .path(ProjectResource.class, "getProject")
                        .build(projecId))
                .rel("project")
                .param("method", "GET")
                .build();
    }

    private Link getProjectOwnerForProject(UriInfo uriInfo, Long projectOwnerId) {
        return Link.fromUri(
                uriInfo.getBaseUriBuilder()
                        .path("user")
                        .path(String.valueOf(projectOwnerId))
                        .build())
                .rel("projectOwner")
                .param("method", "GET")
                .build();
    }

    private Link getSelfLinkForProject(UriInfo uriInfo, Long projectId) {
        return Link.fromUri(
                uriInfo.getBaseUriBuilder()
                        .path("project")
                        .path(String.valueOf(projectId))
                        .build())
                .rel("self")
                .param("method", "GET")
                .build();
    }

    private Link getDeleteLinkForProject(UriInfo uriInfo, Long projectId) {
        return Link.fromUri(
                uriInfo.getBaseUriBuilder()
                        .path("project")
                        .path(String.valueOf(projectId))
                        .build())
                .rel("delete")
                .param("method", "DELETE")
                .build();
    }

    private Link getIssuesLinkForProject(UriInfo uriInfo, Long projectId) {
        return Link.fromUri(
                uriInfo.getBaseUriBuilder()
                        .path("project")
                        .path(String.valueOf(projectId))
                        .path("issues")
                        .build())
                .rel("issues")
                .param("method", "GET")
                .build();
    }
}
