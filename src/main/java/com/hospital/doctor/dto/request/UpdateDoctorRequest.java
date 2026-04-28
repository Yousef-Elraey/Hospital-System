package com.hospital.doctor.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;

import java.time.LocalDateTime;

@Getter
@Setter
@Accessors(chain = true)
public class UpdateDoctorRequest {
    @NotNull(message = "id is required")
    private Long id;

    @NotBlank(message = "name is required")
    private String name;

    @NotBlank(message = "speciality is required")
    private String speciality;

    @NotBlank(message = "contact_number is required")
    @Pattern(regexp = "^(\\+20|0)1[0-9]{9}$", message = "Invalid Egyptian phone number")
    // valid for Egyptian numbers only
    private String contactNumber;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

}
