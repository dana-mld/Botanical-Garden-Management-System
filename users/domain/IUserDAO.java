package com.example.users_service.domain;

import java.util.List;

public interface IUserDAO {

    List<User> users();

    User userById(int id);

    boolean insert(User user);

    boolean update(User user);

    boolean delete(int id);

    User findByUsername(String username);
}