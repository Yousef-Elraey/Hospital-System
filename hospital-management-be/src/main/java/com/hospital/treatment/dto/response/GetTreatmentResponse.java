package com.hospital.treatment.dto.response;

import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;

@Setter
@Getter
@Accessors(chain = true)
public class GetTreatmentResponse {
    private Long id;
    private String nameEn;
    private String nameAr;
    private String activeIngredient;
}
