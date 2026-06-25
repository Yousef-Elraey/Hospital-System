package com.hospital.speciality.dto.response;

import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;
@Setter
@Getter
@Accessors(chain = true)
public class GetSpecialityResponse {

    private Long id;
    private String nameEn;
    private String nameAr;
}
