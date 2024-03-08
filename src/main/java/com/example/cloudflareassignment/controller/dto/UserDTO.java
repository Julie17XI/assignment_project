package com.example.cloudflareassignment.controller.dto;


import jakarta.validation.constraints.*;
import lombok.Data;
import com.example.cloudflareassignment.domain.UserDomain;
@Data
public class UserDTO {

    String id;

    @NotBlank(message = "name cannot be blank")
    String name;

    @NotBlank(message = "email cannot be blank")
    @Email(regexp = "[a-z0-9._%+-]+@[a-z0-9.-]+\\.[a-z]{2,3}", message="Please provide a valid email address")
    String email;

    @NotBlank(message = "password cannot be blank")
    @Size(min = 8, max = 30, message = "Password must be between 8 and 30 characters")
    @Pattern(regexp = "(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z])(?=.*[@#$%^&+=*])(?=\\S+$).{8,}",
            message = "Password must contain at least one digit, one lowercase letter, one uppercase letter, one special character and no whitespace")
    String password;

    public static UserDTO from(UserDomain userDomain) {
        UserDTO userDTO = new UserDTO();
        userDTO.setId(userDomain.getId().toString());
        userDTO.setName(userDomain.getName());
        userDTO.setEmail(userDomain.getEmail());
        userDTO.setPassword(userDomain.getPassword());
        return userDTO;
    }
}
