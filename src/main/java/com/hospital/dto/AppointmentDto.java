package com.hospital.dto;

import com.hospital.entity.AppointmentType;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;
import java.time.LocalDateTime;
import jakarta.validation.constraints.NotNull;


@Getter
@Setter
@Accessors(chain = true)
public class AppointmentDto {
    @NotNull
    private Long id;
    @NotNull(message = "timing is required")
    private LocalDateTime timing;
    @NotNull(message = "appointment_type is required")
    private AppointmentType appointmentType;
    @NotNull(message = "doctor_id is required")
    private Long doctorId;
    @NotNull(message = "patient_id is required")
    private Long patientId;
    @NotNull(message = "createdBy is required")
    private String createdBy;
    private LocalDateTime createdAt;
    @NotNull(message = "updatedBy is required")
    private String updatedBy;
    private LocalDateTime updatedAt;
    private Long statusId;


}
