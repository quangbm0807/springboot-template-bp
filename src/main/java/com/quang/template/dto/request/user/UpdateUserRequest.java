package com.quang.template.dto.request.user;
import com.quang.template.model.Enum.Role;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class UpdateUserRequest {
    private String username;
    private String email;
    private String password; // Optional for updates
    private String firstName;
    private String lastName;
    private Role role;
    private boolean enabled;
    private boolean accountNonLocked;
}