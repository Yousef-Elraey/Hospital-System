package com.hospital.medical_record.dto.request;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Accessors(chain = true)
public class SearchMedicalRecordRequest {
    private Long patientId;
    private Long doctorId;
    private Long diagnoseId;
}
