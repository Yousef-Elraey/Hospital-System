package com.hospital.speciality.dto.request;

import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;

@Setter
@Getter
@Accessors(chain = true)
public class CreateSpecialityRequest {
    private Long id;

    @NotBlank(message = "name is required")
    private String nameEn;

    @NotBlank(message = "name is required")
    private String nameAr;

}
