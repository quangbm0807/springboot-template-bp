package com.quang.template.controller;
import com.quang.template.dto.request.user.CreateUserRequest;
import com.quang.template.dto.request.user.UpdateUserRequest;
import com.quang.template.dto.response.ResponseAPI;
import com.quang.template.model.User;
import com.quang.template.service.UserService;
import com.quang.template.utils.ResponseFactory;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/users")
@RequiredArgsConstructor
@SecurityRequirement(name = "Bearer Authentication")
@Tag(name = "Users", description = "User management API")
public class UserController {

    private final UserService userService;

    @GetMapping
    @Operation(summary = "Get all users")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ResponseAPI> getAllUsers() {
        return ResponseFactory.success(userService.getAllUsers());
    }

    @GetMapping("/{id}")
    @Operation(summary = "Get user by ID")
    @PreAuthorize("hasRole('ADMIN') or @userService.getUserById(#id).username == authentication.name")
    public ResponseEntity<ResponseAPI> getUserById(@PathVariable Long id) {
        return ResponseFactory.success(userService.getUserById(id));
    }

    @GetMapping("/me")
    @Operation(summary = "Get current user information")
    public ResponseEntity<ResponseAPI> getCurrentUser() {
        User user = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        return ResponseFactory.success(userService.getUserById(user.getId()));
    }

    @PostMapping
    @Operation(summary = "Create a new user")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ResponseAPI> createUser(@RequestBody CreateUserRequest request) {
        return ResponseFactory.created(userService.createUser(request));
    }

    @PutMapping("/{id}")
    @Operation(summary = "Update a user")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ResponseAPI> updateUser(
            @PathVariable Long id,
            @RequestBody UpdateUserRequest request) {
        return ResponseFactory.success("User updated successfully", userService.updateUser(id, request));
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Delete a user")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ResponseAPI> deleteUser(@PathVariable Long id) {
        userService.deleteUser(id);
        return ResponseFactory.noContent();
    }
}
