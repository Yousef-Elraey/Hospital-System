package com.hospital.billing.dto.request;

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
    @NotNull(message = "id is required")
    private Long id;
    @NotNull(message = "amount is required")
    private Long amount;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    @NotNull(message = "patient_id is required")
    private Long patient_id;
}
