package com.hospital.diagnose.dto.response;

import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;

@Setter
@Getter
@Accessors(chain = true)
public class GetDiagnoseResponse {
    private Long id;
    private String nameEn;
    private String nameAr;
}
