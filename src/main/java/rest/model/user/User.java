package rest.model.user;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class User {
    Long id;
    String username;

    public User(User user) {
        this.id = user.id;
        this.username = user.username;
    }
}
