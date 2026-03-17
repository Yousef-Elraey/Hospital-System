package com.hospital.dto;

import com.hospital.entity.AppointmentStatus;
import com.hospital.entity.Gender;
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
    private String patientName;
    private Gender patientGender;
    private String patientPhone;
    private LocalDate patientDateOfBirth;
    private String patientCreatedBy;
    private LocalDateTime patientCreatedAt;
    private String patientUpdatedBy;
    private LocalDateTime patientUpdatedAt;
    private LocalDateTime appointmentTiming;
    private Long doctorId;
    private Long patientId;
    private Long statusId;
    private String appointmentCreatedBy;
    private LocalDateTime appointmentCreatedAt;
    private String appointmentUpdatedBy;
    private LocalDateTime appointmentUpdatedAt;


}
