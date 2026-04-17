package com.hospital.billing.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.Accessors;

import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Accessors(chain = true)
public class UpdateBillingRequest {
    private Long id;

    @NotNull(message = "amount is required")
    private Long amount;

    @NotBlank(message = "createdBy is required")
    private String createdBy;
    private LocalDateTime createdAt;

    @NotBlank(message = "updatedBy is required")
    private String updatedBy;
    private LocalDateTime updatedAt;

    @NotNull(message = "patient_id is required")
    private Long patient_id;
}
