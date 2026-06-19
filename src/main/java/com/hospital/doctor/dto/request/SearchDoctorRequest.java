package com.hospital.doctor.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Accessors(chain = true)
public class SearchDoctorRequest {
    @NotBlank(message = "name is required")
    private String name;

    @NotBlank(message = "speciality is required")
    private String speciality;

    @NotBlank(message = "contact_number is required")
    @Pattern(regexp = "^(\\+20|0)1[0-9]{9}$", message = "Invalid Egyptian phone number") // valid for Egyptian numbers only
    private String contactNumber;
}
