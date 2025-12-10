package org.example.resource;

import io.swagger.v3.oas.annotations.tags.Tag;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import org.example.dto.user.CreateUserRequest;
import org.example.dto.user.UserResponse;
import org.example.model.User;
import org.example.service.UserService;
import org.example.exception.ConflictException;
import org.example.exception.NotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

@Tag(name = "Users", description = "Endpoints for user management")
@RestController
@RequestMapping("/users")
public class UserResource {
    private final UserService userService;
    @Autowired
    public UserResource(UserService userService) {
        this.userService = userService;
    }
    @Operation(summary = "Create a new user")
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public UserResponse createUser(@RequestBody CreateUserRequest req) {
        try {
            User user = userService.createUser(req.getName(), req.getEmail());
            return new UserResponse(user);
        } catch (org.springframework.dao.DataIntegrityViolationException ex) {
            throw new ConflictException("USER_EMAIL_DUPLICATE", "A user with this email already exists");
        }
    }
    @Operation(summary = "Get user by ID")
    @GetMapping("/{id}")
    public UserResponse getUser(@Parameter(description="User ID") @PathVariable int id) {
        User user = userService.getUserById(id);
        if (user == null) throw new NotFoundException("USER_NOT_FOUND", "User not found");
        return new UserResponse(user);
    }
}
