package com.hospital.dto;

import com.hospital.entity.Gender;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Past;
import jakarta.validation.constraints.Pattern;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Setter
@Getter
@Accessors(chain = true)
public class PatientDto {
    private Long id;

    @NotBlank(message = "name is required")
    private String name;

    @NotNull(message = "gender is required")
    private Gender gender;

    @NotBlank(message = "phone is required")
    @Pattern(regexp = "^(\\+20|0)1[0-9]{9}$", message = "Invalid Egyptian phone number") // valid for Egyptian numbers only
    private String phone;
    private List<MedicalRecordDto> medicalRecords;

    @Past(message = "Date of birth must be in the past")
    @NotNull(message = "dateOfBirth is required")
    private LocalDate dateOfBirth;

    @NotBlank(message = "createdBy is required")
    private String createdBy;
    private LocalDateTime createdAt;

    @NotBlank(message = "updatedBy is required")
    private String updatedBy;
    private LocalDateTime updatedAt;

}
