package org.example.service.impl;

import org.example.model.User;
import org.example.repository.UserDao;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class UserServiceImplTest {
    private UserDao userDao;
    private UserServiceImpl userService;
    private User testUser;

    @BeforeEach
    void setUp() {
        userDao = mock(UserDao.class);
        userService = new UserServiceImpl(userDao);
        testUser = User.builder().id(1).name("Alice").email("alice@email.com").build();
    }

    @Test
    void testCreateUser_success() {
        doNothing().when(userDao).save(any(User.class));
        User u = userService.createUser("Bob", "bob@email.com");
        assertEquals("Bob", u.getName());
        assertEquals("bob@email.com", u.getEmail());
        verify(userDao).save(any(User.class));
    }

    @Test
    void testGetUserById_found() {
        when(userDao.findById(1)).thenReturn(testUser);
        User u = userService.getUserById(1);
        assertEquals(testUser, u);
    }

    @Test
    void testGetUserById_notFound() {
        when(userDao.findById(404)).thenReturn(null);
        assertNull(userService.getUserById(404));
    }
}
