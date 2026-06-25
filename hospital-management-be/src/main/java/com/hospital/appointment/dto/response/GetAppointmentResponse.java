package com.hospital.appointment.dto.response;

import com.hospital.entity.AppointmentType;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;

import java.time.LocalDateTime;


@Getter
@Setter
@Accessors(chain = true)
public class GetAppointmentResponse {
    private Long id;
    private LocalDateTime timing;
    private AppointmentType appointmentType;
    private Long doctorId;
    private Long patientId;
    private String createdBy;
    private LocalDateTime createdAt;
    private String updatedBy;
    private LocalDateTime updatedAt;
    private Long statusId;


}
