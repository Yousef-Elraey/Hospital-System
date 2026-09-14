package com.hospital.auth.dto.response;

import lombok.Data;
import lombok.experimental.Accessors;

import java.util.Date;

@Data
@Accessors(chain = true)
public class LoginResponse {
    private String token;
    private Date expiresIn;
}
