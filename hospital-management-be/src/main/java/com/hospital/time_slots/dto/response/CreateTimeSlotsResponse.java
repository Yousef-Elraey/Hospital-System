package com.hospital.time_slots.dto.response;

import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;

@Setter
@Getter
@Accessors(chain = true)
public class CreateTimeSlotsResponse {
    private Long id;
}
