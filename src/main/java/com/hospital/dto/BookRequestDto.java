package com.hospital.dto;

import com.hospital.entity.AppointmentType;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Accessors(chain = true)
public class BookRequestDto {
    private Long id;

    @NotNull(message = "appointment_time is required")
    private LocalDateTime appointmentTiming;

    @NotNull(message = "appointment_type is required")
    private AppointmentType appointmentType;

    @NotNull(message = "doctor_id is required")
    private Long doctorId;

    @NotNull(message = "patient_id is required")
    private Long patientId;

    @NotNull(message = "status_id is required")
    private Long statusId;

    @NotBlank(message = "appointment_created_by is required")
    private String appointmentCreatedBy;
    private LocalDateTime appointmentCreatedAt;

    @NotBlank(message = "appointment_updated_by is required")
    private String appointmentUpdatedBy;
    private LocalDateTime appointmentUpdatedAt;


}
