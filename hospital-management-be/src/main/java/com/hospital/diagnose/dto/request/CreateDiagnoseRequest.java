package com.hospital.diagnose.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.Accessors;

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
@Accessors(chain = true)
public class CreateDiagnoseRequest {
    private Long id;

    @NotBlank(message = "name_en is required")
    private String nameEn;
    @NotBlank(message = "name_ar is required")
    private String nameAr;

}
