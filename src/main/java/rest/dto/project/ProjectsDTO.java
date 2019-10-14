package rest.dto.project;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.ws.rs.core.Link;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlElementRef;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@XmlRootElement
public class ProjectsDTO {

    @XmlElementRef
    private List<ProjectDTO> projects;

    @XmlElement
    @XmlJavaTypeAdapter(Link.JaxbAdapter.class)
    private List<Link> links;

}
