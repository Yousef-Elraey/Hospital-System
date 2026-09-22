package com.hospital.time_slots.dto.request;

import com.hospital.entity.AppointmentType;
import com.hospital.entity.TimeSlotsStatus;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

import java.time.LocalDate;
import java.time.LocalTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Accessors(chain = true)
public class SearchTimeSlotsRequest {
    private Long doctorId;
    private AppointmentType appointmentType;
    private LocalDate day;
    private LocalTime start;
    private LocalTime end;
    private TimeSlotsStatus timeSlotsStatus;


}
