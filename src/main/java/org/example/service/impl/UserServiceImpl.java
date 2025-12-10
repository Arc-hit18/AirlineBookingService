package org.example.service.impl;

import org.example.model.User;
import org.example.repository.UserDao;
import org.example.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class UserServiceImpl implements UserService {
    private final UserDao userDao;
    @Autowired
    public UserServiceImpl(UserDao userDao) {
        this.userDao = userDao;
    }
    @Override
    public User createUser(String name, String email) {
        User user = new User();
        user.setName(name);
        user.setEmail(email);
        userDao.save(user);
        return user;
    }
    @Override
    public User getUserById(int id) {
        return userDao.findById(id);
    }
}
