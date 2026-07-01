package com.hospital.timeSlots.dto.response;

import com.hospital.entity.AppointmentType;
import com.hospital.entity.Doctor;
import com.hospital.entity.TimeSlotsStatus;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;

import java.time.LocalDate;
import java.time.LocalTime;

@Setter
@Getter
@Accessors(chain = true)
public class GetTimeSlotsResponse {
    private Long id;
    private Long doctorId;
    private LocalDate day;
    private LocalTime start;
    private LocalTime end;
    private TimeSlotsStatus status;
    private AppointmentType appointmentType;
}
