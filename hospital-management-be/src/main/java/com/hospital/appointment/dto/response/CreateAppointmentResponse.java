package com.hospital.appointment.dto.response;

import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;

@Setter
@Getter
@Accessors(chain = true)
public class CreateAppointmentResponse {
    private Long id;
}
