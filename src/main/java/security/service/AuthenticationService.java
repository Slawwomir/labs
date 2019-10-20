package security.service;

import repository.entities.User;
import security.model.UserCredentials;
import service.UserService;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;

@ApplicationScoped
public class AuthenticationService {

    @Inject
    private UserService userService;

    public User validateCredentials(UserCredentials userCredentials) {
        User user = userService.findUserByName(userCredentials.getUsername());
        String passwordHash = userCredentials.getPasswordHash();

        if (user.getUserCredentials().getPasswordHash().equals(passwordHash)) {
            return user;
        }

        return null;
    }
}
