package com.repairtracker.dto.request;

import javax.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class LoginRequest {
    // Support login by email or username. At least one must be provided.
    private String username;
    private String email;

    @NotBlank(message = "Password is required")
    private String password;
}
