package com.hospital.patient.dto.response;

import com.hospital.entity.Gender;
import com.hospital.medical_record.dto.response.GetMedicalRecordResponse;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
@Setter
@Getter
@Accessors(chain = true)
public class GetPatientResponse {

    private Long id;
    private String name;
    private Gender gender;
    private String phone;
    private List<GetMedicalRecordResponse> medicalRecords;
    private LocalDate dateOfBirth;
    private String createdBy;
    private LocalDateTime createdAt;
    private String updatedBy;
    private LocalDateTime updatedAt;

}
