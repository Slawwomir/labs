package service;

import repository.entities.Role;
import repository.entities.User;
import repository.entities.UserCredentials;
import rest.dto.user.UserDTO;
import security.service.CryptUtils;

import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.sql.Date;
import java.time.ZonedDateTime;
import java.util.List;
import java.util.stream.Collectors;

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

        if (userDTO.getId() != null && userDTO.getRoles() != null) {
            setUserRoles(user, userDTO.getRoles());
        }

        return saveUser(user);
    }

    public User findUserByName(String username) {
        List<User> resultList = entityManager.createNamedQuery("User.findUserByName", User.class)
                .setParameter(1, username)
                .getResultList();

        if (resultList.isEmpty()) {
            return null;
        }

        return resultList.get(0);
    }

    public UserCredentials findUserCredentials(Long userId) {
        return entityManager.createNamedQuery("UserCredentials.findByUserId", UserCredentials.class)
                .setParameter(1, userId)
                .getSingleResult();
    }

    public UserCredentials saveUserCredentials(UserCredentials userCredentials) {
        return entityManager.merge(userCredentials);
    }

    public Role saveRole(Role role) {
        if (role.getId() == null) {
            entityManager.persist(role);
            return role;
        } else {
            return entityManager.merge(role);
        }
    }

    private void setUserRoles(User user, List<String> roles) {
        UserCredentials userCredentials = findUserCredentials(user.getId());
        userCredentials.getRoles().forEach(
                role -> entityManager.remove(role)
        );

        userCredentials.setRoles(roles.stream()
                .map(roleName -> {
                    Role role = new Role();
                    role.setRoleName(roleName);
                    role.setUserCredentials(userCredentials);
                    return role;
                })
                .collect(Collectors.toList())
        );

        userCredentials.setChangedDate(Date.from(ZonedDateTime.now().toInstant()));
        user.setUserCredentials(userCredentials);
    }

    public User createUser(security.model.UserCredentials userCredentials) {
        User user = new User();
        user.setName(userCredentials.getUsername());
        user = saveUser(user);

        UserCredentials userCredentialsEntity = new UserCredentials();
        userCredentialsEntity.setPasswordHash(userCredentials.getPasswordHash());
        userCredentialsEntity.setUser(user);
        userCredentialsEntity = saveUserCredentials(userCredentialsEntity);

        Role role = new Role();
        role.setRoleName("USER");
        role.setUserCredentials(userCredentialsEntity);
        saveRole(role);

        return this.saveUser(user);
    }
}
