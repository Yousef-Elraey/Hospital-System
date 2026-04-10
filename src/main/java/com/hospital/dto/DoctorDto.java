package com.hospital.dto;

import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;

import java.time.LocalDateTime;
@Getter
@Setter
@Accessors(chain = true)
public class DoctorDto {
    private Long id;
   @NotNull(message = "name is required")
    private String name;
    @NotNull(message = "speciality is required")
    private String speciality;
    @NotNull(message = "contact_number is required")
    private String contactNumber;
    @NotNull(message = "createdBy is required")
    private String createdBy;
    private LocalDateTime createdAt;
    @NotNull(message = "updatedBy is required")
    private String updatedBy;
    private LocalDateTime updatedAt;

}
