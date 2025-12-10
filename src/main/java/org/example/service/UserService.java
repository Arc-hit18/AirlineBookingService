package org.example.service;

import org.example.model.User;

/**
 * Service boundary for managing {@link User} entities.
 */
public interface UserService {

    /**
     * Create and persist a new user.
     *
     * @param name  user name
     * @param email user email address
     * @return created user
     */
    User createUser(String name, String email);

    /**
     * Fetch a user by identifier.
     *
     * @param id user identifier
     * @return user, or {@code null} if not found
     */
    User getUserById(int id);
}
