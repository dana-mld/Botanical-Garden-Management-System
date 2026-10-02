package com.example.users_service.infrastructure;

import com.example.users_service.domain.IUserDAO;
import com.example.users_service.domain.User;
import com.example.users_service.infrastructure.tableEntities.UserEntity;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.TypedQuery;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Repository
public class UserDAO implements IUserDAO {

    @PersistenceContext
    private EntityManager entityManager;

    @Override
    public List<User> users() {
        return entityManager
                .createQuery("SELECT u FROM UserEntity u", UserEntity.class)
                .getResultList()
                .stream()
                .map(UserEntity::toUser)
                .collect(Collectors.toList());
    }

    @Override
    public User userById(int id) {
        UserEntity entity = entityManager.find(UserEntity.class, id);
        return entity != null ? entity.toUser() : null;
    }
@Override
@Transactional
    public boolean insert(User user) {
        System.out.println("=== UserDAO.insert ===");
        System.out.println("Hash before persist: " + user.getPasswordHash());

        UserEntity entity = toEntity(user);
        System.out.println("Entity hash: " + entity.getPasswordHash());

        entityManager.persist(entity);
        return true;
    }

    @Override
    @Transactional
    public boolean update(User user) {
        try {
            UserEntity entity = entityManager.find(UserEntity.class, user.getId().getId());
            if (entity == null) return false;

            entity.setUsername(user.getUsername());
            entity.setEmail(user.getEmail());
            entity.setRole(user.getRole());
            entity.setActive(user.isActive());

            if (user.getPasswordHash() != null && !user.getPasswordHash().isEmpty()) {
                entity.setPasswordHash(user.getPasswordHash());
            }

            if (user.getPhoneNumber() != null && !user.getPhoneNumber().isEmpty()) {
                entity.setPhoneNumber(user.getPhoneNumber());
            }

            entityManager.merge(entity);
            return true;
        } catch (Exception e) {
            System.err.println(" Update error: " + e.getMessage());
            return false;
        }
    }
    @Override
    @Transactional
    public boolean delete(int id) {
        try {
            UserEntity entity = entityManager.find(UserEntity.class, id);
            if (entity != null) {
                entityManager.remove(entity);
                return true;
            }
            return false;
        } catch (Exception e) {
            return false;
        }
    }

    @Override
    public User findByUsername(String username) {
        try {
            TypedQuery<UserEntity> query = entityManager.createQuery(
                    "SELECT u FROM UserEntity u WHERE u.username = :username",
                    UserEntity.class
            );
            query.setParameter("username", username);

            UserEntity entity = query.getSingleResult();
            return entity != null ? entity.toUser() : null;
        } catch (Exception e) {
            return null;
        }
    }

    private UserEntity toEntity(User user) {
        UserEntity entity = new UserEntity();

        if (user.getId() != null && user.getId().getId() != 0) {
            entity.setId(user.getId().getId());
        }

        entity.setUsername(user.getUsername());
        entity.setEmail(user.getEmail());
        entity.setPasswordHash(user.getPasswordHash());
        entity.setRole(user.getRole());
        entity.setActive(user.isActive());
        entity.setPhoneNumber(user.getPhoneNumber());

        return entity;
    }
}