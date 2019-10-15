package rest.dto.project;

import lombok.Data;
import lombok.NoArgsConstructor;
import repository.entities.Project;
import rest.validation.annotations.ProjectExists;
import rest.validation.annotations.UserExists;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import javax.ws.rs.core.Link;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter;
import java.util.List;


@Data
@NoArgsConstructor
@XmlRootElement
public class ProjectDTO {

    @XmlElement
    @ProjectExists
    private Long id;

    @XmlElement
    @NotNull(message = "project name must be set")
    @Size(min = 2, max = 25, message = "Project name must be longer than 1 character and shorter than 25 characters")
    private String name;

    @XmlElement
    @UserExists
    private Long projectOwnerId;

    @XmlElement
    @XmlJavaTypeAdapter(Link.JaxbAdapter.class)
    private List<Link> links;

    public ProjectDTO(Project project) {
        this.id = project.getId();
        this.name = project.getName();
        this.projectOwnerId = project.getProjectOwner().getId();
        this.links = List.of();
    }
}
