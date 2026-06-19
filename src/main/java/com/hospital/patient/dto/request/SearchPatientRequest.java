package com.hospital.patient.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Past;
import jakarta.validation.constraints.Pattern;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

import java.time.LocalDate;
@Data
@AllArgsConstructor
@NoArgsConstructor
@Accessors(chain = true)
public class SearchPatientRequest {
    @NotBlank(message = "name is required")
    private String name;

    @Past(message = "Date of birth must be in the past")
    @NotNull(message = "dateOfBirth is required")
    private LocalDate dateOfBirth;

    @NotBlank(message = "phone is required")
    @Pattern(regexp = "^(\\+20|0)1[0-9]{9}$", message = "Invalid Egyptian phone number")
    // valid for Egyptian numbers only
    private String phone;
}
