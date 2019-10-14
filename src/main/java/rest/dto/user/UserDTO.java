package rest.dto.user;

import lombok.Data;
import repository.entities.User;

import javax.validation.constraints.Size;
import javax.ws.rs.core.Link;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@Data
@XmlRootElement
public class UserDTO {

    @XmlElement
    private Long id;

    @XmlElement
    @Size(min = 3, max=20, message = "username must be longer than 2 characters and shorter than 21 characters.")
    private String username;

    @XmlElement
    @XmlJavaTypeAdapter(Link.JaxbAdapter.class)
    private List<Link> links;

    public UserDTO(UserDTO user) {
        this.id = user.id;
        this.username = user.username;
        this.links = user.links == null ? Collections.emptyList() : new ArrayList<>(user.links);
    }

    public UserDTO(User user) {
        this.id = user.getId();
        this.username = user.getName();
        this.links = Collections.emptyList();
    }
}
