package com.hospital.dto;

import com.hospital.entity.Gender;
import com.hospital.entity.MedicalRecord;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Date;
import java.util.List;

@Setter
@Getter
@Accessors(chain = true)
public class PatientDto {
    private Long id;
    private String name;
    private Gender gender;
    private String phone;
    private List<MedicalRecordDto> medicalRecords;
    private LocalDate dateOfBirth;
    private String createdBy;
    private LocalDateTime createdAt;
    private String updatedBy;
    private LocalDateTime updatedAt;

}
