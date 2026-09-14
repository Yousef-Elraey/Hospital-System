package com.hospital.user.dto.request;

import com.hospital.entity.Role;
import com.hospital.entity.Status;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;

@Getter
@Setter
@Accessors(chain = true)
public class CreateUserRequest {
    @NotBlank(message = "userName is required")
    private String userName;

    @NotBlank(message = "phone is required")
    private String phone;

    @NotBlank(message = "email is required")
    @Email(message = "Please provide a valid email address")
    private String email;

    @NotBlank(message = "address is required")
    private String address;

    @NotBlank(message = "summary is required")
    private String summary;

    private Status status;

    private Boolean active;

    @NotBlank(message = "password is required")
    private String password;

    @NotBlank(message = "fullName is required")
    private String fullName;

    private Role role;
}
