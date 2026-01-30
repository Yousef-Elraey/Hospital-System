package com.HospitalManagementSystem.HospitalSystem.dto;


import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;

import java.time.LocalDateTime;
import java.util.Date;

@Getter
@Setter
@Accessors(chain = true)
public class AppointmentDto {
    private Long id;
    private LocalDateTime timing;
    private Long doctorId;
    private Long patientId;
    private String createdBy;
    private LocalDateTime createdAt;
    private String updatedBy;
    private LocalDateTime updatedAt;


}
