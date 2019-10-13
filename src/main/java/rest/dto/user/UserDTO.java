package rest.dto.user;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import repository.entities.User;

import javax.ws.rs.core.Link;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@XmlRootElement
public class UserDTO {
    @XmlElement
    private Long id;
    @XmlElement
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
