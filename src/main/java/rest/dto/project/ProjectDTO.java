package rest.dto.project;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import repository.entities.Project;
import rest.validation.annotations.UserExists;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import javax.ws.rs.core.Link;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter;
import java.util.ArrayList;
import java.util.List;


@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@XmlRootElement
public class ProjectDTO {

    @XmlElement
    private Long id;

    @XmlElement
    @NotNull(message = "project name must be set")
    @Size(min = 2, max = 10, message = "Project name must be longer than 1 character and shorter than 11 characters")
    private String name;

    @XmlElement
    @UserExists
    private Long projectOwnerId;

    @XmlElement
    @XmlJavaTypeAdapter(Link.JaxbAdapter.class)
    private List<Link> links;

    public ProjectDTO(ProjectDTO project) {
        this.id = project.id;
        this.name = project.name;
        this.projectOwnerId = project.projectOwnerId;
        this.links = project.links == null ? List.of() : new ArrayList<>(project.links);
    }

    public ProjectDTO(Project project) {
        this.id = project.getId();
        this.name = project.getName();
        this.projectOwnerId = project.getProjectOwner() == null ? null : project.getProjectOwner().getId();
        this.links = List.of();
    }
}
