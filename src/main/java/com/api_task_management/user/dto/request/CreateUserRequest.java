package com.api_task_management.user.dto.request;

import com.api_task_management.user.dto.type.UserRole;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;

import java.time.LocalDateTime;

@Data
public class CreateUserRequest {
    @NotBlank(message = "Email is a required field !")
    @Size(min = 5 , max = 250 , message =  "Email should be in 10 to 20 characters")
    private String email;

    @NotBlank(message = "Password is a required field !")
    private String password;

    @NotBlank(message = "First Name is a required field !")
    @Size(min = 4 , max = 250 , message =  "First should be in 4 to 20 characters")
    private String firstName;
    @NotBlank(message = "Last Name is a required field !")
    @Size(min = 4 , max = 250 , message =  "First should be in 4 to 20 characters")
    private String lastName;
}
