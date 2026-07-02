package com.hospital.appointment.dto.response;

import com.hospital.entity.AppointmentType;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

import java.time.LocalDateTime;
@Data
@Accessors(chain = true)
@AllArgsConstructor
@NoArgsConstructor
public class SearchAppointmentResponse {
    private Long id;
    private LocalDateTime timing;
    private AppointmentType appointmentType;
    private String doctorName;
    private String patientName;
    private String createdBy;
    private LocalDateTime createdAt;
    private String updatedBy;
    private LocalDateTime updatedAt;
    private String statusName;
}
