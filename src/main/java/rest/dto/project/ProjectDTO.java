package rest.dto.project;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import repository.entities.Project;

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
    private String name;
    @XmlElement
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
        this.projectOwnerId = project.getProjectOwner().getId();
        this.links = List.of();
    }
}
