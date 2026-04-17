package com.hospital.medicalRecord.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.Accessors;

import java.time.LocalDateTime;

@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Accessors(chain = true)
public class CreateMedicalRecordRequest {
    private Long id;

    @NotBlank(message = "diagnose is required")
    private String diagnose;

    @NotBlank(message = "treatment is required")
    private String treatment;

    @NotNull(message = "patient_id is required")
    private Long patientId;

    @NotNull(message = "doctor_id is required")
    private Long doctorId;
    private LocalDateTime createdAt;

    @NotBlank(message = "createdBy is required")
    private String createdBy;
    private LocalDateTime updatedAt;

    @NotBlank(message = "updatedBy is required")
    private String updatedBy;

}
