package com.hospital.users.dto.response;

import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;

@Getter
@Setter
@Accessors(chain = true)
public class GetUserResponse {
    private Long id;
    private String userName;
    private String password;
}
