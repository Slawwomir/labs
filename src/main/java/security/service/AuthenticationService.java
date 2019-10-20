package security.service;

import repository.entities.User;
import security.model.UserCredentials;
import service.UserService;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import java.sql.Date;
import java.time.ZonedDateTime;

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

    public User changePassword(Long userId, String password) {
        User user = userService.findUser(userId);
        user.getUserCredentials().setPasswordHash(CryptUtils.sha256(password));
        user.getUserCredentials().setChangedDate(Date.from(ZonedDateTime.now().toInstant()));
        userService.saveUserCredentials(user.getUserCredentials());
        return user;
    }
}
