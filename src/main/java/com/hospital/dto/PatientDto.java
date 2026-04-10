package com.hospital.dto;

import com.hospital.entity.Gender;
import com.hospital.entity.MedicalRecord;
import jakarta.validation.constraints.NotNull;
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
    @NotNull(message = "name is required")
    private String name;
    @NotNull(message = "gender is required")
    private Gender gender;
    @NotNull(message = "phone is required")
    private String phone;
    private List<MedicalRecordDto> medicalRecords;
    @NotNull(message = "dateOfBirth is required")
    private LocalDate dateOfBirth;
    @NotNull(message = "createdBy is required")
    private String createdBy;
    private LocalDateTime createdAt;
    @NotNull(message = "updatedBy is required")
    private String updatedBy;
    private LocalDateTime updatedAt;

}
