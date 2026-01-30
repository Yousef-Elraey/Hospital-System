package com.HospitalManagementSystem.HospitalSystem.dto;

import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;

import java.time.LocalDateTime;
import java.util.Date;

@Setter
@Getter
@Accessors(chain = true)
public class PatientDto {
    private Long id;
    private String name;
    private String gender;
    private String phone;
    private Date dateOfBirth;
    private String createdBy;
    private LocalDateTime createdAt;
    private String updatedBy;
    private LocalDateTime updatedAt;

}
