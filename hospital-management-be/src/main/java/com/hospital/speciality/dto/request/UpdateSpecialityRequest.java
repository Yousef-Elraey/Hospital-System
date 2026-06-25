package com.hospital.speciality.dto.request;

import com.hospital.entity.Gender;
import com.hospital.medicalRecord.dto.request.CreateMedicalRecordRequest;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Past;
import jakarta.validation.constraints.Pattern;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Setter
@Getter
@Accessors(chain = true)
public class UpdateSpecialityRequest {
    private Long id;

    @NotBlank(message = "name is required")
    private String name_en;

    @NotBlank(message = "name is required")
    private String name_ar;

}
