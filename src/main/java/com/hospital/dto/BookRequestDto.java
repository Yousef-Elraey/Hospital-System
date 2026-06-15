package com.hospital.dto;

import com.hospital.entity.AppointmentType;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
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
    @Min(value = 1, message = "status_id must be at least 1")
    @Max(value = 5, message = "status_id must not exceed 5")
    private Long statusId;
    private LocalDateTime appointmentCreatedAt;
    private LocalDateTime appointmentUpdatedAt;


}
