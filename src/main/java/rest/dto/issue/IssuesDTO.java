package rest.dto.issue;

import lombok.AllArgsConstructor;
import lombok.Data;

import javax.ws.rs.core.Link;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter;
import java.util.List;

@Data
@AllArgsConstructor
@XmlRootElement
public class IssuesDTO {

    @XmlElement
    private List<IssueDTO> issues;

    @XmlElement
    @XmlJavaTypeAdapter(Link.JaxbAdapter.class)
    private List<Link> links;

}
