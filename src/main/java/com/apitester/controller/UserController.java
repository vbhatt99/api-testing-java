package com.apitester.controller;

import com.apitester.model.User;
import com.apitester.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;

/**
 * REST Controller for User Management
 * Provides endpoints for user CRUD operations
 */
@RestController
@RequestMapping("/users")
@RequiredArgsConstructor
@Slf4j
@Tag(name = "User Management", description = "APIs for managing users")
public class UserController {
    
    private final UserService userService;
    
    /**
     * Create a new user
     */
    @PostMapping
    @Operation(summary = "Create a new user", description = "Creates a new user with the provided details")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "201", description = "User created successfully"),
        @ApiResponse(responseCode = "400", description = "Invalid input data"),
        @ApiResponse(responseCode = "409", description = "Username or email already exists")
    })
    public ResponseEntity<User> createUser(
            @Parameter(description = "User details", required = true)
            @Valid @RequestBody User user) {
        log.info("POST /users - Creating user: {}", user.getUsername());
        try {
            User createdUser = userService.createUser(user);
            return ResponseEntity.status(HttpStatus.CREATED).body(createdUser);
        } catch (RuntimeException e) {
            if (e.getMessage().contains("already exists")) {
                log.warn("Conflict detected: {}", e.getMessage());
                return ResponseEntity.status(HttpStatus.CONFLICT).build();
            }
            throw e; // Re-throw other runtime exceptions
        }
    }
    
    /**
     * Get user by ID
     */
    @GetMapping("/{id}")
    @Operation(summary = "Get user by ID", description = "Retrieves a user by their ID")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "User found"),
        @ApiResponse(responseCode = "404", description = "User not found")
    })
    public ResponseEntity<User> getUserById(
            @Parameter(description = "User ID", required = true)
            @PathVariable Long id) {
        log.info("GET /users/{} - Fetching user by ID", id);
        return userService.getUserById(id)
                .map(user -> ResponseEntity.ok(user))
                .orElse(ResponseEntity.notFound().build());
    }
    
    /**
     * Get user by username
     */
    @GetMapping("/username/{username}")
    @Operation(summary = "Get user by username", description = "Retrieves a user by their username")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "User found"),
        @ApiResponse(responseCode = "404", description = "User not found")
    })
    public ResponseEntity<User> getUserByUsername(
            @Parameter(description = "Username", required = true)
            @PathVariable String username) {
        log.info("GET /users/username/{} - Fetching user by username", username);
        return userService.getUserByUsername(username)
                .map(user -> ResponseEntity.ok(user))
                .orElse(ResponseEntity.notFound().build());
    }
    
    /**
     * Get user by email
     */
    @GetMapping("/email/{email}")
    @Operation(summary = "Get user by email", description = "Retrieves a user by their email")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "User found"),
        @ApiResponse(responseCode = "404", description = "User not found")
    })
    public ResponseEntity<User> getUserByEmail(
            @Parameter(description = "Email address", required = true)
            @PathVariable String email) {
        log.info("GET /users/email/{} - Fetching user by email", email);
        return userService.getUserByEmail(email)
                .map(user -> ResponseEntity.ok(user))
                .orElse(ResponseEntity.notFound().build());
    }
    
    /**
     * Get all users
     */
    @GetMapping
    @Operation(summary = "Get all users", description = "Retrieves all users")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Users retrieved successfully")
    })
    public ResponseEntity<List<User>> getAllUsers() {
        log.info("GET /users - Fetching all users");
        List<User> users = userService.getAllUsers();
        return ResponseEntity.ok(users);
    }
    
    /**
     * Get users by status
     */
    @GetMapping("/status/{status}")
    @Operation(summary = "Get users by status", description = "Retrieves users by their status")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Users retrieved successfully")
    })
    public ResponseEntity<List<User>> getUsersByStatus(
            @Parameter(description = "User status", required = true)
            @PathVariable User.UserStatus status) {
        log.info("GET /users/status/{} - Fetching users by status", status);
        List<User> users = userService.getUsersByStatus(status);
        return ResponseEntity.ok(users);
    }
    
    /**
     * Get active users
     */
    @GetMapping("/active")
    @Operation(summary = "Get active users", description = "Retrieves all active users")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Active users retrieved successfully")
    })
    public ResponseEntity<List<User>> getActiveUsers() {
        log.info("GET /users/active - Fetching active users");
        List<User> users = userService.getActiveUsers();
        return ResponseEntity.ok(users);
    }
    
    /**
     * Update user
     */
    @PutMapping("/{id}")
    @Operation(summary = "Update user", description = "Updates an existing user")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "User updated successfully"),
        @ApiResponse(responseCode = "404", description = "User not found"),
        @ApiResponse(responseCode = "409", description = "Username or email already exists")
    })
    public ResponseEntity<User> updateUser(
            @Parameter(description = "User ID", required = true)
            @PathVariable Long id,
            @Parameter(description = "Updated user details", required = true)
            @Valid @RequestBody User userDetails) {
        log.info("PUT /users/{} - Updating user", id);
        try {
            User updatedUser = userService.updateUser(id, userDetails);
            return ResponseEntity.ok(updatedUser);
        } catch (RuntimeException e) {
            if (e.getMessage().contains("already exists")) {
                log.warn("Conflict detected: {}", e.getMessage());
                return ResponseEntity.status(HttpStatus.CONFLICT).build();
            } else if (e.getMessage().contains("not found")) {
                return ResponseEntity.notFound().build();
            }
            throw e; // Re-throw other runtime exceptions
        }
    }
    
    /**
     * Delete user
     */
    @DeleteMapping("/{id}")
    @Operation(summary = "Delete user", description = "Deletes a user by their ID")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "204", description = "User deleted successfully"),
        @ApiResponse(responseCode = "404", description = "User not found")
    })
    public ResponseEntity<Void> deleteUser(
            @Parameter(description = "User ID", required = true)
            @PathVariable Long id) {
        log.info("DELETE /users/{} - Deleting user", id);
        try {
            userService.deleteUser(id);
            return ResponseEntity.noContent().build();
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        }
    }
    
    /**
     * Search users by first name
     */
    @GetMapping("/search/firstname")
    @Operation(summary = "Search users by first name", description = "Searches users by first name (case-insensitive)")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Users found")
    })
    public ResponseEntity<List<User>> searchUsersByFirstName(
            @Parameter(description = "First name to search for", required = true)
            @RequestParam String firstName) {
        log.info("GET /users/search/firstname?firstName={} - Searching users by first name", firstName);
        List<User> users = userService.searchUsersByFirstName(firstName);
        return ResponseEntity.ok(users);
    }
    
    /**
     * Search users by last name
     */
    @GetMapping("/search/lastname")
    @Operation(summary = "Search users by last name", description = "Searches users by last name (case-insensitive)")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Users found")
    })
    public ResponseEntity<List<User>> searchUsersByLastName(
            @Parameter(description = "Last name to search for", required = true)
            @RequestParam String lastName) {
        log.info("GET /users/search/lastname?lastName={} - Searching users by last name", lastName);
        List<User> users = userService.searchUsersByLastName(lastName);
        return ResponseEntity.ok(users);
    }
    
    /**
     * Get users created after a specific date
     */
    @GetMapping("/created-after")
    @Operation(summary = "Get users created after date", description = "Retrieves users created after a specific date")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Users found")
    })
    public ResponseEntity<List<User>> getUsersCreatedAfter(
            @Parameter(description = "Date to filter from (ISO format)", required = true)
            @RequestParam String date) {
        log.info("GET /users/created-after?date={} - Fetching users created after date", date);
        LocalDateTime dateTime = LocalDateTime.parse(date);
        List<User> users = userService.getUsersCreatedAfter(dateTime);
        return ResponseEntity.ok(users);
    }
    
    /**
     * Get users by email domain
     */
    @GetMapping("/email-domain/{domain}")
    @Operation(summary = "Get users by email domain", description = "Retrieves users by email domain")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Users found")
    })
    public ResponseEntity<List<User>> getUsersByEmailDomain(
            @Parameter(description = "Email domain", required = true)
            @PathVariable String domain) {
        log.info("GET /users/email-domain/{} - Fetching users by email domain", domain);
        List<User> users = userService.getUsersByEmailDomain(domain);
        return ResponseEntity.ok(users);
    }
}
