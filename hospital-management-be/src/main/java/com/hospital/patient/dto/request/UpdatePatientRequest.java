package com.hospital.patient.dto.request;

import com.hospital.entity.Gender;
import com.hospital.medicalRecord.dto.request.CreateMedicalRecordRequest;
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
public class UpdatePatientRequest {
    @NotNull(message = "id is required")
    private Long id;

    @NotBlank(message = "name is required")
    private String name;

    @NotNull(message = "gender is required")
    private Gender gender;

    @NotBlank(message = "phone is required")
    @Pattern(regexp = "^(\\+20|0)1[0-9]{9}$", message = "Invalid Egyptian phone number") // valid for Egyptian numbers only
    private String phone;
    private List<CreateMedicalRecordRequest> medicalRecords;

    @Past(message = "Date of birth must be in the past")
    @NotNull(message = "dateOfBirth is required")
    private LocalDate dateOfBirth;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

}
