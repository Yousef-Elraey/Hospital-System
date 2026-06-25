package com.hospital.speciality.dto.request;

import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;

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
