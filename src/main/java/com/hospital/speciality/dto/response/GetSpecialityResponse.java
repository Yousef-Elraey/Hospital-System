package com.hospital.speciality.dto.response;

import com.hospital.entity.Gender;
import com.hospital.medicalRecord.dto.response.GetMedicalRecordResponse;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
@Setter
@Getter
@Accessors(chain = true)
public class GetSpecialityResponse {

    private Long id;
    private String name_en;
    private String name_ar;
}
