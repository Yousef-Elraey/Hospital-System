package com.HospitalManagementSystem.HospitalSystem.dto;

import com.HospitalManagementSystem.HospitalSystem.entity.MedicalRecord;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;

import java.time.LocalDateTime;
import java.util.Date;
import java.util.List;

@Setter
@Getter
@Accessors(chain = true)
public class PatientDto {
    private Long id;
    private String name;
    private String gender;
    private String phone;
    private List<MedicalRecordDto> medicalRecords;
    private Date dateOfBirth;
    private String createdBy;
    private LocalDateTime createdAt;
    private String updatedBy;
    private LocalDateTime updatedAt;

}
