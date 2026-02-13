package com.HospitalManagementSystem.HospitalSystem.dto;

import com.HospitalManagementSystem.HospitalSystem.entity.AppointmentStatus;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Accessors(chain = true)
public class BookResponseDto {
    private int numberOfWaiting;
    private String status;
}
