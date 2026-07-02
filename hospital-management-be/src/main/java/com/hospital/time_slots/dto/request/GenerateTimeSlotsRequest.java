package com.hospital.time_slots.dto.request;

import com.hospital.entity.AppointmentType;
import com.hospital.entity.TimeSlotsStatus;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.Accessors;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

@Setter
@Getter
@Accessors(chain = true)
@AllArgsConstructor
@NoArgsConstructor
public class GenerateTimeSlotsRequest {

    private LocalTime start;
    private LocalTime end;
    private LocalDate dayStart;
    private LocalDate dayEnd;
    private Long doctorId;
    private Long duration;
    private List<String> days;
    private AppointmentType appointmentType;
    private TimeSlotsStatus status = TimeSlotsStatus.AVAILABLE;

}
