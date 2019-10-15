package service;

import repository.entities.User;
import rest.dto.user.UserDTO;

import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.util.Arrays;
import java.util.List;

@Stateless
public class UserService {

    @PersistenceContext
    private EntityManager entityManager;

    public List<User> findAllUsers() {
        return entityManager.createNamedQuery("User.findAll", User.class).getResultList();
    }

    public List<User> findUsers(int start, int max) {
        return entityManager.createNamedQuery("User.findAll", User.class)
                .setFirstResult(start)
                .setMaxResults(max)
                .getResultList();
    }

    public Long getUsersCount() {
        return entityManager.createQuery("SELECT COUNT(u) FROM User u", Long.class).getSingleResult();
    }

    public User findUser(Long id) {
        if (id == null) {
            return null;
        }
        return entityManager.find(User.class, id);
    }

    public synchronized User saveUser(User user) {
        if (user.getId() != null) {
            entityManager.merge(user);
        } else {
            entityManager.persist(user);
        }

        return user;
    }

    public void removeUser(Long userId) {
        entityManager.remove(findUser(userId));
    }

    public User saveUser(UserDTO userDTO) {
        User user = new User();
        user.setId(userDTO.getId());
        user.setName(userDTO.getUsername());

        return saveUser(user);
    }
}
