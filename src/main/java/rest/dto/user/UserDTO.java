package rest.dto.user;

import lombok.Data;
import lombok.NoArgsConstructor;
import repository.entities.Role;
import repository.entities.User;
import rest.validation.annotations.UserExists;

import javax.validation.constraints.Size;
import javax.ws.rs.core.Link;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

@Data
@NoArgsConstructor
@XmlRootElement
public class UserDTO {

    @XmlElement
    @UserExists
    private Long id;

    @XmlElement
    @Size(min = 3, max = 20, message = "username must be longer than 2 characters and shorter than 21 characters.")
    private String username;

    @XmlElement
    @XmlJavaTypeAdapter(Link.JaxbAdapter.class)
    private List<Link> links;

    @XmlElement
    private List<String> roles;

    public UserDTO(User user) {
        this.id = user.getId();
        this.username = user.getName();
        this.roles = user.getUserCredentials().getRoles().stream().map(Role::getRoleName).collect(Collectors.toList());
        this.links = Collections.emptyList();
    }
}
