package com.hospital.treatment.dto.request;

import lombok.*;
import lombok.experimental.Accessors;

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
@Accessors(chain = true)
public class SearchTreatmentRequest {
    private String nameEn;
    private String nameAr;
    private String activeIngredient;

}
