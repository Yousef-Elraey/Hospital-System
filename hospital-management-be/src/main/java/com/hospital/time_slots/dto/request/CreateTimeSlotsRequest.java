package com.hospital.time_slots.dto.request;
import com.hospital.entity.AppointmentType;
import com.hospital.entity.TimeSlotsStatus;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.Accessors;

import java.time.LocalDate;
import java.time.LocalTime;

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
@Accessors(chain = true)
public class CreateTimeSlotsRequest {
    private Long id;

    @NotNull(message = "doctor_id is required")
    private Long doctorId;

    @NotNull(message = "day is required")
    private LocalDate day;

    @NotNull(message = "time is required")
    private LocalTime start;

    @NotNull(message = "time is required")
    private LocalTime end;

    @NotNull(message = "status is required")
    private TimeSlotsStatus status;

    @NotNull(message = "appointment type is required")
    private AppointmentType appointmentType;

}
