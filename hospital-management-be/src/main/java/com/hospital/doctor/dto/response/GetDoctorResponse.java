package com.hospital.doctor.dto.response;

import com.hospital.entity.Speciality;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;

import java.time.LocalDateTime;

@Getter
@Setter
@Accessors(chain = true)
public class GetDoctorResponse {
    private Long id;
    private String name;
    private Speciality speciality;
    private String contactNumber;
    private String createdBy;
    private LocalDateTime createdAt;
    private String updatedBy;
    private LocalDateTime updatedAt;

}
