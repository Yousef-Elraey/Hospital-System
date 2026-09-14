package com.hospital.user.dto.response;

import com.hospital.entity.Role;
import com.hospital.entity.Status;
import jakarta.persistence.Column;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;

@Getter
@Setter
@Accessors(chain = true)
public class GetUserResponse {
    private String userName;
    private String phone;
    private String email;
    private String fullName;
    private String address;
    private String summary;
    private Role role;
    private Boolean active;
    private Status status;
}
