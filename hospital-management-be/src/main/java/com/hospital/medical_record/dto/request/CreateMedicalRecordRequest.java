package com.hospital.medical_record.dto.request;

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

    @NotNull(message = "diagnose is required")
    private Long diagnoseId;

    @NotNull(message = "treatment is required")
    private Long treatmentId;

    @NotNull(message = "patient_id is required")
    private Long patientId;

    @NotNull(message = "doctor_id is required")
    private Long doctorId;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}
