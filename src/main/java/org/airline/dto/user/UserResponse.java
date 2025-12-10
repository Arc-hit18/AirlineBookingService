package org.airline.dto.user;

import org.airline.model.User;

public class UserResponse {
    private int id;
    private String name;
    private String email;
    public UserResponse(User u) {
        this.id = u.getId();
        this.name = u.getName();
        this.email = u.getEmail();
    }
    public int getId() { return id; }
    public String getName() { return name; }
    public String getEmail() { return email; }
}

