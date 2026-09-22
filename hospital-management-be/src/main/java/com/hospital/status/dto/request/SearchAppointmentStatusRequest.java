package com.hospital.status.dto.request;

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
public class SearchAppointmentStatusRequest {
    private Long id;
    private String nameEn;
    private String nameAr;
}
