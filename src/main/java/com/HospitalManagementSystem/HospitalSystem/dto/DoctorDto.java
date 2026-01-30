package com.HospitalManagementSystem.HospitalSystem.dto;

import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;

import java.time.LocalDateTime;
@Getter
@Setter
@Accessors(chain = true)
public class DoctorDto {
    private Long id;
    private String name;
    private String speciality;
    private String contactNumber;
    private String createdBy;
    private LocalDateTime createdAt;
    private String updatedBy;
    private LocalDateTime updatedAt;

}
