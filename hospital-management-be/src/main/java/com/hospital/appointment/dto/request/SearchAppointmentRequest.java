package com.hospital.appointment.dto.request;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

import java.time.LocalDate;
@Data
@AllArgsConstructor
@NoArgsConstructor
@Accessors(chain = true)
public class SearchAppointmentRequest {
    private Long patientId;
    private Long DoctorId;
    private Long StatusId;
    private LocalDate date;
}
