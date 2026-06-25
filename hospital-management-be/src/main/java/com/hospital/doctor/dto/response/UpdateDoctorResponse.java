package com.hospital.doctor.dto.response;

import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;

@Setter
@Getter
@Accessors(chain = true)
public class UpdateDoctorResponse {
    private Long id;
}
