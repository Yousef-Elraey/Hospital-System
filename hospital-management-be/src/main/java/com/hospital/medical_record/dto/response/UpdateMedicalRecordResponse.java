package com.hospital.medical_record.dto.response;

import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;

@Setter
@Getter
@Accessors(chain = true)
public class UpdateMedicalRecordResponse {
    private Long id;
}
