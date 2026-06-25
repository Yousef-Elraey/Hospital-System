package com.hospital.appointment.dto.request;

import com.hospital.entity.AppointmentType;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;

import java.time.LocalDateTime;


@Getter
@Setter
@Accessors(chain = true)
public class CreateAppointmentRequest {
    private Long id;

    @NotNull(message = "timing is required")
    private LocalDateTime timing;

    @NotNull(message = "appointment_type is required")
    private AppointmentType appointmentType;

    @NotNull(message = "doctor_id is required")
    private Long doctorId;

    @NotNull(message = "patient_id is required")
    private Long patientId;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    @NotNull(message = "status_id is required")
    private Long statusId;


}
