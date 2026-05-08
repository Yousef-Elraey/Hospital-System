package com.hospital.users.dto.request;

import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;

@Getter
@Setter
@Accessors(chain = true)
public class CreateUserRequest {
    @NotBlank(message = "userName is required")
    String userName;
    @NotBlank(message = "password is required")
    String password;
}
