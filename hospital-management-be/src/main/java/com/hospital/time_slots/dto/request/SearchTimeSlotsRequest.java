package com.hospital.time_slots.dto.request;

import com.hospital.entity.TimeSlotsStatus;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

import java.time.LocalDate;
@Data
@AllArgsConstructor
@NoArgsConstructor
@Accessors(chain = true)
public class SearchTimeSlotsRequest {
    private Long doctorId;
    private TimeSlotsStatus timeSlotsStatus;
    private LocalDate from;
    private LocalDate to;
}
