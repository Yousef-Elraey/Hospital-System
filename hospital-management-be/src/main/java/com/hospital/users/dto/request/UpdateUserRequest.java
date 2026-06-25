package com.hospital.users.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;

@Getter
@Setter
@Accessors(chain = true)
public class UpdateUserRequest {
    @NotNull(message = "id is required")
    private Long id;
    @NotBlank(message = "userName is required")
    private String userName;
    @NotBlank(message = "password is required")
    private String password;
}
