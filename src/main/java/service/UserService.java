package service;

import rest.model.user.User;

import javax.annotation.PostConstruct;
import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@ApplicationScoped
public class UserService {

    @Inject
    private IssueService issueService;

    private final List<User> users = new ArrayList<>();

    @PostConstruct
    public void init() {
        users.add(new User(1L, "Andriu", null));
        users.add(new User(2L, "Krystyna", null));
    }

    public List<User> findAllUsers() {
        return users.stream().map(User::new).collect(Collectors.toList());
    }

    public User findUser(Long id) {
        return users.stream().filter(p -> p.getId().equals(id)).findFirst().orElse(null);
    }

    public synchronized User saveUser(User user) {
        if (user.getId() != null) {
            users.removeIf(p -> p.getId().equals(user.getId()));
            users.add(user);
        } else {
            user.setId(users.stream().mapToLong(User::getId).max().orElse(0) + 1);
            users.add(new User(user));
        }

        return user;
    }

    public void removeUser(User user) {
        removeUser(user.getId());
    }

    public void removeUser(Long userId) {
        users.removeIf(p -> p.getId().equals(userId));
    }
}
