package org.example.repository;

import org.example.model.User;

public interface UserDao {
    void save(User user);
    User findById(int id);
}
