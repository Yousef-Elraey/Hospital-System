package com.hospital.dto;

import com.hospital.entity.AppointmentStatus;
import com.hospital.entity.AppointmentType;
import com.hospital.entity.Gender;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Date;

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
    @NotNull(message = "appointment_created_by is required")
    private String appointmentCreatedBy;
    private LocalDateTime appointmentCreatedAt;
    @NotNull(message = "appointment_updated_by is required")
    private String appointmentUpdatedBy;
    private LocalDateTime appointmentUpdatedAt;


}
