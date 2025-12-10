package org.example.repository;

import org.example.model.User;

/**
 * Data access abstraction for {@link User} entities.
 */
public interface UserDao {

    /**
     * Persist a new user.
     *
     * @param user user to be saved
     */
    void save(User user);

    /**
     * Find a user by its primary key.
     *
     * @param id user identifier
     * @return matching user, or {@code null} if none found
     */
    User findById(int id);
}
