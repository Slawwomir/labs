package service;

import repository.entities.User;
import rest.dto.user.UserDTO;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.transaction.Transactional;
import java.util.List;

@ApplicationScoped
public class UserService {

    @Inject
    private IssueService issueService;

    @PersistenceContext
    private EntityManager entityManager;

    @Transactional
    public List<User> findAllUsers() {
        return entityManager.createNamedQuery("User.findAll", User.class).getResultList();
    }

    @Transactional
    public User findUser(Long id) {
        return entityManager.find(User.class, id);
    }

    @Transactional
    public synchronized User saveUser(User user) {
        if (user.getId() != null) {
            entityManager.merge(user);
        } else {
            entityManager.persist(user);
        }

        return user;
    }

    @Transactional
    public void removeUser(User user) {
        entityManager.remove(user);
    }

    public void removeUser(Long userId) {
        User user = new User();
        user.setId(userId);
        removeUser(user);
    }

    public User saveUser(UserDTO userDTO) {
        User user = new User();
        user.setId(userDTO.getId());
        user.setName(userDTO.getUsername());

        return saveUser(user);
    }
}
