package com.hospital.patient.dto.response;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;

@Getter
@Setter
@Accessors(chain = true)
public class CreatePatientResponse {
    private Long id;

}
