package rest.dto.user;

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
public class UsersDTO {

    @XmlElement
    private List<UserDTO> users;

    @XmlElement
    @XmlJavaTypeAdapter(Link.JaxbAdapter.class)
    private List<Link> links;

}
