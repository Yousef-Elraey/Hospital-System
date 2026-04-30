package com.hospital.medicalRecord.dto.response;

import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;

@Setter
@Getter
@Accessors(chain = true)
public class CreateMedicalRecordResponse {
    private Long id;
}
