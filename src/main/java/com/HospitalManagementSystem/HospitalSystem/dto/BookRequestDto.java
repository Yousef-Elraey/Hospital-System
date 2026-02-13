package com.HospitalManagementSystem.HospitalSystem.dto;

import com.HospitalManagementSystem.HospitalSystem.entity.AppointmentStatus;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

import java.time.LocalDateTime;
import java.util.Date;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Accessors(chain = true)
public class BookRequestDto {
    private Long id;
    private String patientName;
    private String patientGender;
    private String patientPhone;
    private Date patientDateOfBirth;
    private String patientCreatedBy;
    private LocalDateTime patientCreatedAt;
    private String patientUpdatedBy;
    private LocalDateTime patientUpdatedAt;
    private LocalDateTime appointmentTiming;
    private Long doctorId;
    private Long patientId;
    private String status;
    private String appointmentCreatedBy;
    private LocalDateTime appointmentCreatedAt;
    private String appointmentUpdatedBy;
    private LocalDateTime appointmentUpdatedAt;


}
