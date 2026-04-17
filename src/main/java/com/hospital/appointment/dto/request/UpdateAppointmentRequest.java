package com.hospital.appointment.dto.request;

import com.hospital.entity.AppointmentType;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;

import java.time.LocalDateTime;


@Getter
@Setter
@Accessors(chain = true)
public class UpdateAppointmentRequest {
    private Long id;

    @NotNull(message = "timing is required")
    private LocalDateTime timing;

    @NotNull(message = "appointment_type is required")
    private AppointmentType appointmentType;

    @NotNull(message = "doctor_id is required")
    private Long doctorId;

    @NotNull(message = "patient_id is required")
    private Long patientId;

    @NotBlank(message = "createdBy is required")
    private String createdBy;
    private LocalDateTime createdAt;

    @NotBlank(message = "updatedBy is required")
    private String updatedBy;
    private LocalDateTime updatedAt;
    @NotNull(message = "status_id is required")
    private Long statusId;


}
