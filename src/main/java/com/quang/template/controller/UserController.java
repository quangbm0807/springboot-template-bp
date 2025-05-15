package com.quang.template.controller;
import com.quang.template.dto.request.user.CreateUserRequest;
import com.quang.template.dto.request.user.UpdateUserRequest;
import com.quang.template.dto.response.ResponseAPI;
import com.quang.template.model.Enum.Role;
import com.quang.template.model.User;
import com.quang.template.service.UserService;
import com.quang.template.utils.ResponseFactory;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
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
    @Operation(
            summary = "Get all users with pagination",
            description = "Returns a paginated list of all users. Requires ADMIN role."
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successfully retrieved users"),
            @ApiResponse(responseCode = "403", description = "Access denied"),
    })
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ResponseAPI> getAllUsers(
            @Parameter(description = "Page number (0-based)")
            @RequestParam(defaultValue = "0") int page,

            @Parameter(description = "Number of items per page")
            @RequestParam(defaultValue = "10") int size,

            @Parameter(description = "Sort field")
            @RequestParam(defaultValue = "id") String sortBy,

            @Parameter(description = "Sort direction (asc or desc)")
            @RequestParam(defaultValue = "asc") String sortDir,

            HttpServletRequest request) {

        return ResponseFactory.success(userService.getUsersWithPagination(page, size, sortBy, sortDir, request));
    }

    @GetMapping("/search")
    @Operation(
            summary = "Search and filter users",
            description = "Search users by keyword and/or filter by role with pagination. Requires ADMIN role."
    )
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ResponseAPI> searchUsers(
            @Parameter(description = "Search keyword for username, email, or name")
            @RequestParam(required = false) String keyword,

            @Parameter(description = "Filter by role")
            @RequestParam(required = false) Role role,

            @Parameter(description = "Page number (0-based)")
            @RequestParam(defaultValue = "0") int page,

            @Parameter(description = "Number of items per page")
            @RequestParam(defaultValue = "10") int size,

            @Parameter(description = "Sort field")
            @RequestParam(defaultValue = "id") String sortBy,

            @Parameter(description = "Sort direction (asc or desc)")
            @RequestParam(defaultValue = "asc") String sortDir,

            HttpServletRequest request) {

        return ResponseFactory.success(
                userService.searchUsers(keyword, role, page, size, sortBy, sortDir, request)
        );
    }

    @GetMapping("/{id}")
    @Operation(
            summary = "Get user by ID",
            description = "Returns a single user by ID. User can access their own info, ADMIN can access any user."
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "User found"),
            @ApiResponse(responseCode = "404", description = "User not found"),
            @ApiResponse(responseCode = "403", description = "Access denied")
    })
    @PreAuthorize("hasRole('ADMIN') or @userService.getUserById(#id).username == authentication.name")
    public ResponseEntity<ResponseAPI> getUserById(
            @Parameter(description = "User ID", required = true)
            @PathVariable Long id) {
        return ResponseFactory.success(userService.getUserById(id));
    }

    @GetMapping("/me")
    @Operation(
            summary = "Get current user information",
            description = "Returns the currently authenticated user's information"
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "User information retrieved successfully")
    })
    public ResponseEntity<ResponseAPI> getCurrentUser() {
        User user = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        return ResponseFactory.success(userService.getUserById(user.getId()));
    }

    @PostMapping
    @Operation(
            summary = "Create a new user",
            description = "Creates a new user with specified details. Requires ADMIN role."
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "User created successfully"),
            @ApiResponse(responseCode = "400", description = "Invalid input or username/email already exists"),
            @ApiResponse(responseCode = "403", description = "Access denied")
    })
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ResponseAPI> createUser(
            @Valid @RequestBody CreateUserRequest request) {
        return ResponseFactory.created(userService.createUser(request));
    }

    @PutMapping("/{id}")
    @Operation(
            summary = "Update a user",
            description = "Updates an existing user's details. Requires ADMIN role."
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "User updated successfully"),
            @ApiResponse(responseCode = "400", description = "Invalid input or username/email already exists"),
            @ApiResponse(responseCode = "404", description = "User not found"),
            @ApiResponse(responseCode = "403", description = "Access denied")
    })
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ResponseAPI> updateUser(
            @Parameter(description = "User ID", required = true)
            @PathVariable Long id,
            @Valid @RequestBody UpdateUserRequest request) {
        return ResponseFactory.success("User updated successfully", userService.updateUser(id, request));
    }

    @DeleteMapping("/{id}")
    @Operation(
            summary = "Delete a user",
            description = "Deletes a user by ID. Requires ADMIN role."
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "User deleted successfully"),
            @ApiResponse(responseCode = "404", description = "User not found"),
            @ApiResponse(responseCode = "403", description = "Access denied")
    })
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ResponseAPI> deleteUser(
            @Parameter(description = "User ID", required = true)
            @PathVariable Long id) {
        userService.deleteUser(id);
        return ResponseFactory.noContent();
    }
}